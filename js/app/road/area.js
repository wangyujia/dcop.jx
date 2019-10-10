/// area.js


var area = (function () {
    load('base.js');
    load('logs.js');
    load('coor.js');
    load('line.js');
    load('size.js');
    load('range.js');
    load('stub.js');
    load('roll.js');
    load('work.js');

    var m_stubs = stub();
    var m_works = {};
    var m_rolls = {};

    var o = {
        left_bottom: true,      // 绝对坐标是否从左下开始 (地理坐标是这样的)
        near_range: 100,        // 靠近范围
        road_width: 33,         // 路面宽度
        divi_width: 3,          // 隔离带宽度
        toString: function() {
            return "{size:(0x" + this.road_width + 
                ",divw:" + this.divi_width + ",unit:20}";
        }
    };

    add_obj_method(o, 'init', function(road_width, divi_width) {
        if (road_width) this.road_width = road_width;
        if (divi_width) this.divi_width = divi_width;

        logs('area').record("global(v1.0.46) area: " + this + "\r\n  " + 
            "grid: {size:(0.15x0.15),count:(0x" + (road_width/0.15) + ")}");
    });

    add_obj_method(o, 'stub_add', function(x, y) {
        m_stubs.add(x, y);
    });

    add_obj_method(o, 'stub_add', function(m, x, y) {
        m_stubs.add(m, x, y);
    });

    add_obj_method(o, 'stub_end', function() {
        m_stubs.dump();
    });

    add_obj_method(o, 'stubs', function() {
        return m_stubs;
    });

    add_obj_method(o, 'roll_add', function(id, name, type, length, width, left, top, end) {
        if (m_rolls[id]) return;
        m_rolls[id] = roll(id, name, type, length, width, left, top, end);
    });

    add_obj_method(o, 'roll_del', function(id) {
        if (!m_rolls[id]) return;
        delete m_rolls[id];
    });

    add_obj_method(o, 'roll_clear', function() {
        for (var a in m_rolls) {
            delete m_rolls[a];
        }
    });

    add_obj_method(o, 'work_find', function(x, y) {
        var r = m_stubs.where(x, y);
        if (!r) return null;
        for (var a in m_works) {
            var work = m_works[a];
            if (work && work.inside(r.coor.x, r.coor.y)) {
                return work;
            }
        }

        return null;
    });

    add_obj_method(o, 'where_roll', function(id, x, y, time, speed, temperature, vibration) {
        var roll = m_rolls[id];
        if (!roll) return;
        
    });

    return o;
})();
