/// coor.js


/**
 * 生成坐标对象
 */
function coor() {
    var o = {
        x: 0,
        y: 0,
        toString: function() {
            return "(" + this.x + "," + this.y + ")";
        },
        toStringSvg: function(x, y, scale) {
            if (!x) x = 0;
            if (!y) y = 0;
            if (!scale) scale = 1;
            return "" + Math.round((this.x-x)*scale) + 
                "," + Math.round((this.y-y)*scale);
        }
    };

    add_obj_method(o, 'update', function(x, y) {
        this.x = (!x)? 0 : x;
        this.y = (!y)? 0 : y;
    });

    add_obj_method(o, 'update', function(c) {
        this.x = (!c)? 0 : c.x;
        this.y = (!c)? 0 : c.y;
    });

    add_obj_method(o, 'equals', function(x, y) {
        return ((x == this.x) && (y == this.y));
    });

    add_obj_method(o, 'equals', function(c) {
        if (!c) return false;
        return ((c.x == this.x) && (c.y == this.y));
    });

    return o.update.apply(o, arguments);
}
