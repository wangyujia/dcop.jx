/// size.js


/**
 * 生成大小对象
 */
function size() {
    var o = {
        length: 0,
        width: 0,
        toString: function() {
            return "(" + this.length + "x" + this.width + ")";
        }
    };

    add_obj_method(o, 'update', function(l, w) {
        this.length = (!l)? 0 : l;
        this.width  = (!w)? 0 : w;
    });

    add_obj_method(o, 'update', function(s) {
        this.length = (!s)? 0 : s.length;
        this.width  = (!s)? 0 : s.width;
    });

    add_obj_method(o, 'equals', function(l, w) {
        return ((l == this.length) && (w == this.width));
    });

    add_obj_method(o, 'equals', function(s) {
        if (!s) return false;
        return ((s.length == this.length) && (s.width == this.width));
    });

    return o.update.apply(o, arguments);
}
