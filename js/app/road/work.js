/// work.js


/**
 * 生成工作对象
 */
function work() {
    var o = {
        id: id,                 // 工作区域ID
        start: false,           // 是否开始工作
        finish: false,          // 是否结束工作
        part: 0,                // -1:左幅;0:未确定幅度;1:右幅
        direct: 0,              // -1:向左;0:未确定方向;1:向右
        begin: -1,              // 开始位置
        end: -1,                // 结束位置
        work_width: 0,          // 施工宽度
        work_length: 0          // 施工长度
    };

    add_obj_method(o, 'init', function(id, width) {
        this.id = id;
        if (width) {
            this.work_width = width;
        } else {
            this.work_width = area.road_width/2 - area.divi_width/2;
        }
    });

    /// 判断坐标是否在工作区域范围内
    add_obj_method(o, 'inside', function(x, y) {
        if (!this.start) {
            /// 未开始工作，不能判断
            return false;
        }
        if (this.begin < 0) {
            /// 未确定开始位置，不能判断
            return false;
        }
        if (this.part < 0) {
            /// 如果是左幅施工，Y坐标不能到右幅范围
            if (y > (area.road_width/2 + area.divi_width/2)) return false;
        } else if (this.part > 0) {
            /// 如果是右幅施工，Y坐标不能到左幅范围
            if (y < (area.road_width/2 - area.divi_width/2)) return false;
        }
        if (this.direct < 0) {
            /// 如果是向左施工，X坐标不能超过左侧施工范围和右侧
            if (x < (this.begin - this.work_length - area.near_range)) return false;
            if (x > (this.begin + area.near_range)) return false;
        } else if (this.direct > 0) {
            if (x < (this.begin - area.near_range)) return false;
            if (x > (this.begin + this.work_length + area.near_range)) return true;
        } else {
            /// 未确定方向，不能判断
            return false;
        }

        return true;
    });

    return o.init.apply(o, arguments);
}
