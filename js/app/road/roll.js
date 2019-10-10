/// roll.js


/**
 * 生成压路机对象
 */
function roll() {
    var o = {
        id: "",                 // ID
        name: "",               // 名字
        type: "",               // 类型
        size: size(),           // 大小
        sensor: coor(),         // 传感器位置
        begin: false,           // 是否开始机器 (比如摊铺机)
        end: false,             // 是否收尾机器
        work: null,             // 所属工作区域
        time: 0,                // 时间
        speed: 0,               // 速度
        temperature: 0,         // 温度
        vibration: "none",      // 震动
    };

    add_obj_method(o, 'init', function(id, name, type, length, width, left, top, end) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size.update(length, width);
        this.sensor.update(left, top);
        if (type == "tanpu") this.begin = true;
        if (typeof(end) == "boolean") this.end = end;
    });

    add_obj_method(o, 'where', function(id, x, y, time, speed, temperature, vibration) {
        if (!this.work) {
            var work = area.work_find(x, y);
            if (!work) return;
            this.work = work;
        }

        
    });

    return o.init.apply(o, arguments);
}
