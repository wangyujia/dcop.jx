/// range.js


/**
 * 生成范围对象
 */
function range() {
    var o = {
        left_top: coor(),
        right_top: coor(),
        left_bottom: coor(),
        right_bottom: coor(),
        size: size(),
        toString: function() {
            return "{" + this.left_top + 
                ","   + this.right_top + 
                ","   + this.left_bottom + 
                ","   + this.right_bottom + "}";
        },
        toStringSvg: function(x, y, scale) {
            return "" + this.left_top.toStringSvg(x, y, scale) + 
                " "   + this.right_top.toStringSvg(x, y, scale) + 
                " "   + this.right_bottom.toStringSvg(x, y, scale) + 
                " "   + this.left_bottom.toStringSvg(x, y, scale);
        }
    };

    add_obj_method(o, 'update', function(line, width_top, width_bottom) {
        if (!line || (line.L == 0)) return;
        if (typeof(width_bottom) == "undefined") {
            width_bottom   = width_top / 2;
            width_top      = width_top / 2;
        }
        var width_top_x    = Math.abs(width_top    * line.Y / line.L);
        var width_top_y    = Math.abs(width_top    * line.X / line.L);
        var width_bottom_x = Math.abs(width_bottom * line.Y / line.L);
        var width_bottom_y = Math.abs(width_bottom * line.X / line.L);
        if ((line.X >= 0) && (line.Y >= 0)) {
            this.left_top.update(line.start.x    + width_top_x,    line.start.y - width_top_y);
            this.right_top.update(line.end.x     + width_top_x,    line.end.y   - width_top_y);
            this.left_bottom.update(line.start.x - width_bottom_x, line.start.y + width_bottom_y);
            this.right_bottom.update(line.end.x  - width_bottom_x, line.end.y   + width_bottom_y);
        } else if ((line.X >= 0) && (line.Y < 0)) {
            this.left_top.update(line.start.x    - width_top_x,    line.start.y - width_top_y);
            this.right_top.update(line.end.x     - width_top_x,    line.end.y   - width_top_y);
            this.left_bottom.update(line.start.x + width_bottom_x, line.start.y + width_bottom_y);
            this.right_bottom.update(line.end.x  + width_bottom_x, line.end.y   + width_bottom_y);
        } else if ((line.X < 0) && (line.Y < 0)) {
            this.left_top.update(line.start.x    - width_top_x,    line.start.y + width_top_y);
            this.right_top.update(line.end.x     - width_top_x,    line.end.y   + width_top_y);
            this.left_bottom.update(line.start.x + width_bottom_x, line.start.y - width_bottom_y);
            this.right_bottom.update(line.end.x  + width_bottom_x, line.end.y   - width_bottom_y);
        } else if ((line.X < 0) && (line.Y >= 0)) {
            this.left_top.update(line.start.x    + width_top_x,    line.start.y + width_top_y);
            this.right_top.update(line.end.x     + width_top_x,    line.end.y   + width_top_y);
            this.left_bottom.update(line.start.x - width_bottom_x, line.start.y - width_bottom_y);
            this.right_bottom.update(line.end.x  - width_bottom_x, line.end.y   - width_bottom_y);
        }
        this.size.update(line.L, width_top + width_bottom);
    });

    add_obj_method(o, 'equals', function(rn) {
        if (!rn) return false;
        if (!this.left_top.equals(rn.left_top)) return false;
        if (!this.right_top.equals(rn.right_top)) return false;
        if (!this.left_bottom.equals(rn.left_bottom)) return false;
        if (!this.right_bottom.equals(rn.right_bottom)) return false;
        return true;
    });

    return o.update.apply(o, arguments);
}