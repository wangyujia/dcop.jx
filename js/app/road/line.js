/// line.js



/**
 * 生成直线对象
 */
function line() {
    var o = {
        start:  coor(),
        end:    coor(),
        X:      0,
        Y:      0,
        L:      0,
        toString: function() {
            return "|(" + this.start + 
                "[X:"   + this.X + 
                ",Y:"   + this.Y + 
                ",L:"   + this.L + 
                "]"     + this.end + ")|";
        }
    };

    add_obj_method(o, 'compute', function() {
        this.X = this.end.x - this.start.x;
        this.Y = this.end.y - this.start.y;
        this.L = Math.sqrt(this.X*this.X + this.Y*this.Y);
    });

    add_obj_method(o, 'update', function(x1, y1, x2, y2) {
        this.start.update(x1, y1);
        this.end.update(x2, y2);
        return o.compute();
    });

    add_obj_method(o, 'update', function(c1, c2) {
        this.start.update(c1);
        this.end.update(c2);
        return o.compute();
    });

    add_obj_method(o, 'update', function(ln) {
        this.start.update((!ln)? null : ln.start);
        this.end.update((!ln)? null : ln.end);
        return o.compute();
    });

    add_obj_method(o, 'equals', function(x1, y1, x2, y2) {
        if (!this.start.equals(x1, y1)) return false;
        if (!this.end.equals(x2, y2)) return false;
        return true;
    });

    add_obj_method(o, 'equals', function(c1, c2) {
        if (!this.start.equals(c1)) return false;
        if (!this.end.equals(c2)) return false;
        return true;
    });

    add_obj_method(o, 'equals', function(ln) {
        if (!ln) return false;
        if (!this.start.equals(ln.start)) return false;
        if (!this.end.equals(ln.end)) return false;
        return true;
    });

    return o.update.apply(o, arguments);
}