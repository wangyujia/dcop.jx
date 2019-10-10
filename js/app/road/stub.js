/// stub.js


/**
 * 生成桩对象
 */
function stub() {
    var m_start = 0;
    var m_min_x = 0;
    var m_max_x = 0;
    var m_min_y = 0;
    var m_max_y = 0;
    var m_total = 0;
    var m_coors = [];
    var m_lines = [];

    /// 线段对象
    var node = function() {
        var o = {
            index:  "",
            mark:   0,
            line:   line(),
            toString: function() {
                return "{" + this.index + ": " + this.line + "}";
            }
        };

        add_obj_method(o, 'update', function() {
            this.line.update.apply(this.line, arguments);
        });

        add_obj_method(o, 'match', function(x, y) {
            var X = this.line.X;
            var Y = this.line.Y;
            var L = this.line.L;
            var a = x - this.line.start.x;
            var b = (X != 0)? (a * Y / X) : 0;
            var c = (X != 0)? Math.sqrt(a*a + b*b) : 0;
            var l = y - this.line.start.y - b;
            var m = (L != 0)? (l * Y / L) : 0;
            var n = (L != 0)? (l * X / L) : 0;
            m = (X > 0)? ((a > 0)? (m + c) : (m - c)) : ((a < 0)? (m + c) : (m - c));
            n = (X != 0)? n : (0 - a);
            if (area) {
                if (area.left_bottom) n = area.road_width / 2 - n;
                else n = area.road_width / 2 + n;
            }
            return coor(m, n);
        });

        return o.update.apply(o, arguments);
    }

    /// 生成最后的线段
    var make_last_line = function(m) {
        if (m_coors.length < 2) return;
        var c1 = m_coors[m_coors.length - 2];
        var c2 = m_coors[m_coors.length - 1];
        var ln = node(c1, c2);
        ln.index = m_coors.length - 2;
        if (typeof(m) == "undefined") {
            ln.mark = m_total; // get_float_fixed(m_total, 2); // m_total.toFixed(2);
        } else {
            ln.mark = m;
        }
        m_lines[m_lines.length] = ln;
        m_total += ln.line.L;
    }

    /// 获取范围
    var get_min_max_xy = function(x, y) {
        if ((m_min_x == 0) || (x < m_min_x)) {
            m_min_x = x;
        }
        if ((m_min_y == 0) || (y < m_min_y)) {
            m_min_y = y;
        }
        if ((m_max_x == 0) || (x > m_max_x)) {
            m_max_x = x;
        }
        if ((m_max_y == 0) || (y > m_max_y)) {
            m_max_y = y;
        }
    }

    var o = {};

    add_obj_method(o, 'start', function() {
        return m_start;
    });

    add_obj_method(o, 'start', function(mark) {
        m_start = mark;
    });

    add_obj_method(o, 'min_x', function() {
        return m_min_x;
    });

    add_obj_method(o, 'min_y', function() {
        return m_min_y;
    });

    add_obj_method(o, 'max_x', function() {
        return m_max_x;
    });

    add_obj_method(o, 'max_y', function() {
        return m_max_y;
    });

    add_obj_method(o, 'range', function() {
        return {
            min_x: m_min_x,
            min_y: m_min_y,
            max_x: m_max_x,
            max_y: m_max_y,
            toString: function() {
                return "{" + this.min_x + 
                    "," + this.min_y + 
                    "," + this.max_x + 
                    "," + this.max_y + 
                    "}";
            }
        };
    });

    add_obj_method(o, 'total', function() {
        return m_total;
    });

    add_obj_method(o, 'coors', function(i) {
        return m_coors[i];
    });

    add_obj_method(o, 'coors_count', function() {
        return m_coors.length;
    });

    add_obj_method(o, 'lines', function(i) {
        return m_lines[i];
    });

    add_obj_method(o, 'lines_count', function() {
        return m_lines.length;
    });

    add_obj_method(o, 'add', function(x, y, m) {
        m_coors[m_coors.length] = coor(x, y);
        get_min_max_xy(x, y);
        make_last_line(m);
    });

    add_obj_method(o, 'dump', function(id) {
        logs('stub').record('work(' + id + 
            ') end stub: {count:' + m_lines.length + '}');
        for (var i = 0; i < m_lines.length; ++i) {
            var ln = m_lines[i];
            logs('stub').record('{index:' + ln.index + 
                ',mark:' + ln.mark + 
                ',line:' + ln.line + '}');
        }
        logs('stub').record("range: " + this.range());
    });

    /// 检查结果
    add_obj_method(o, 'check', function(i, x, y) {
        if (!i) i = 0;
        if ((i < 0) || (i >= m_lines.length)) {
            return false;
        }

        var n = m_lines[i];
        var l = n.line.L;
        if (!l) {
            /// 长度为0的线段无法进行三角运算，所以结果都是不准确的
            return false;
        }
        if (i > 0) {
            /// 只有第一个线段才允许x超出左边界，其他都不能为负
            if (x < 0) return false;
        }
        if (i < (m_lines.length - 1)) {
            /// 只有最后一个线段才允许x超出右边界，其他都不能超过线段长度
            if (x > l) return false;
        }
        if (area.near_range > 0) {
            /// 如果设置了靠近范围，y需要在范围内才能正确
            if (y < (area.road_width / 2 - area.near_range)) return false;
            if (y > (area.road_width / 2 + area.near_range)) return false;
        }

        return true;
    });

    /// 往前遍历
    add_obj_method(o, 'lookback', function(k, x, y) {
        if (!k) k = 0;
        if ((k < 0) || (k >= m_lines.length)) {
            return null;
        }

        for (var i = k; i >= 0; --i) {
            var n = m_lines[i];
            var l = n.line.L;
            var p = n.match(x, y);
            if (this.check(i, p.x, p.y)) {
                return {index: i, coor: coor(p.x+n.mark, p.y)};
            }
        }

        return null;
    });

    /// 往后遍历
    add_obj_method(o, 'lookfor', function(k, x, y) {
        if (!k) k = 0;
        if ((k < 0) || (k >= m_lines.length)) {
            return null;
        }

        for (var i = k; i < m_lines.length; ++i) {
            var n = m_lines[i];
            var l = n.line.L;
            var p = n.match(x, y);
            if (this.check(i, p.x, p.y)) {
                return {index: i, coor: coor(p.x+n.mark, p.y)};
            }
        }

        return null;
    });

    /// 把绝对坐标转换为相对坐标
    add_obj_method(o, 'where', function(x, y, i) {
        if (!i) i = 0;
        if ((i < 0) || (i >= m_lines.length)) {
            return null;
        }

        /// 判断X轴投影
        var n = m_lines[i];
        var l = n.line.L;
        var p = n.match(x, y);
        if (this.check(i, p.x, p.y)) {
            return {index: i, coor: coor(p.x+n.mark, p.y)};
        }

        /// 需要循环遍历
        if (p.x < 0) {
            var r = this.lookback(i-1, x, y);
            if (r == null) {
                r = this.lookfor(i+1, x, y);
            }
            return r;
        } else {
            var r = this.lookfor(i+1, x, y);
            if (r == null) {
                r = this.lookback(i-1, x, y);
            }
            return r;
        }

        return null;
    });

    /// 反向转换相对坐标到绝对坐标
    add_obj_method(o, 'reverse', function(x, y) {
        var i = 0;
        for (i = 0; i < m_lines.length; ++i) {
            var n = m_lines[i];
            var l = n.line.L;
            if (x <= n.line.L) {
                break;
            }

            x -= l;
        }

        if (i >= m_lines.length) {
            return null;
        }

        var n = m_lines[i];
        var X = n.line.X;
        var Y = n.line.Y;
        var L = n.line.L;

        var rel_x = x;
        var rel_y = (area.road_width / 2 - y);
        var rel_l = rel_y * Math.abs(L / X);

        var m = rel_y * Math.abs(Y / X);
        var c = rel_x - m;
        var a = c * Math.abs(X / L);
        var b = c * Math.abs(Y / L);

        b = b + rel_l;

        var p_x = (X > 0) ? (n.line.start.x + a) : (n.line.start.x - a);
        var p_y = (area.left_bottom)? 
                  ((Y > 0)? (n.line.start.y + b) : (n.line.start.y - b)) : 
                  ((Y < 0)? (n.line.start.y + b) : (n.line.start.y - b));
        return coor(p_x, p_y);
    });

    return o;
}
