/// base.js

/**
 * 为对象添加函数 (利用闭包对参数数量不同的函数进行重载)
 * @param {Object} o 目的对象
 * @param {String} a 方法名称
 * @param {Function} f 方法函数
 */
function add_obj_method(o, a, f) {
    var t = o[a];

    o[a] = function() {
        var r;
        if (f.length === arguments.length) {
            r = f.apply(this, arguments);
        } else if (typeof(t) === 'function') {
            r = t.apply(this, arguments);
        } else {
            r = f.apply(this, arguments);
        }

        return (typeof(r) == 'undefined')? this : r;
    }
}


/**
 * 获取函数名称
 * @param {Function} f 函数
 */
function get_fun_name(f) {
    if (typeof(f) == 'function' || typeof(f) == 'object') {
        var name = ('' + func).match(/function\s*([\w\$]*)\s*\(/);
    }

    return name && name[1];
}


/**
 * 获取函数调用栈
 */
function get_call_stack() {
    var s = [];
    var caller = arguments.callee.caller;
    // print('callee: ' + arguments.callee);
    // print('caller: ' + get_call_stack.caller);

    while (caller) {
        s.unshift(get_fun_name(caller));
        caller = caller && caller.caller;
    }

    return s;
}


/**
 * 获取保留指定位数的浮点数
 * @param {Number} number 被处理的浮点数
 * @param {Number} n 要保留的小数位数
 */
function get_float_fixed(number, n) {
    n = n ? parseInt(n) : 0;
    if (n <= 0) return Math.round(number);
    number = Math.round(number * Math.pow(10, n)) / Math.pow(10, n);
    return number;
};


/**
 * 打印对象
 */
function print_obj_member(o, f) {
    if (!f) f = print;
    if (typeof(o) != "object") {
        return f(o);
    }

    f(Object.prototype.toString.call(o));
    for (var a in o) {
        f("  " + a + ": " + o[a]);
    }
}
