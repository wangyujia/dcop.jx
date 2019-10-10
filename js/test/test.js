/// test.js


var test_all = (function() {
    load('area.js');
    area.init();

    var all = {};

    return function() {
        return all;
    };
})();

function TEST_CASE(a, f) {
    logs('test').record("add test case '" + a + "'");
    add_obj_method(test_all(), a, f);
}

function test() {
    var t = {success:0,failure:0,first_fail:""};
    var o = test_all();
    var i = 0;
    for (var a in o) {
        var f = o[a];
        logs('test').record("=========== TEST '" + a + "' START ===========");
        if (f) {
            var r = f();
            if ((typeof(r) == "boolean") && (!r)) {
                if (!t.first_fail.length) t.first_fail = a;
                logs('test').record("(testcase '" + a + "' failed!)");
                t.failure++;
            } else {
                t.success++;
            }
        }
        logs('test').record("=========== TEST '" + a + "' END =============");
        ++i;
    }

    logs('test').record("TOTAL COUNT: " + i);
    logs('test').record("SUCCESS: " + t.success);
    logs('test').record("FAILURE: " + t.failure);
    if (t.failure > 0) {
        logs('test').record("(first failed: '" + t.first_fail + "')");
    }
}


/// =================================================================
TEST_CASE('test_func', function() {
    var o = {};
    add_obj_method(o, 'f', function() {
        logs('test').record('f()');
    });
    add_obj_method(o, 'f', function(a) {
        logs('test').record('f(' + a + ')');
    });
    add_obj_method(o, 'f', function(a,b) {
        logs('test').record('f(' + a + ',' + b + ')');
    });
    o.f().f(1).f(1,2).f(1,2,3);
});
/// =================================================================
TEST_CASE('test_coor', function() {
    var a = coor(1.1, 2.2);
    var b = coor(0.1, 0.2);
    logs('test').record(a);
    logs('test').record(b);
    logs('test').record(a.equals(b));
    if (a.equals(b)) return false;
    a.update(b);
    logs('test').record(a);
    logs('test').record(b);
    logs('test').record(a.equals(b));
    if (!a.equals(b)) return false;
    a.update(3,3);
    logs('test').record(a);
    if (!a.equals(3,3)) return false;
});
/// =================================================================
TEST_CASE('test_line', function() {
    var b = line(1,1, 1,3);
    var a = line(1,1, 3,1);
    var c = line(1,1, 3,3);
    logs('test').record(a);
    logs('test').record(b);
    logs('test').record(c);
    a.update(c);
    logs('test').record(a);
    if (!a.equals(c)) return false;
});
/// =================================================================
TEST_CASE('test_fib', function() {
    function fib_norm_main(n) {
        for (i = 0; i < n; i++) {
            // logs('test').record(' ' + fib_norm(i));
            fib_norm(i);
        }
    }
    function fib_tail_main(n) {
        for (i = 0; i < n; i++) {
            // logs('test').record(' ' + fib_tail(i));
            fib_tail(i);
        }
    }
    function fib_norm(n) {
        if (n == 0) { return 0; }
        if (n == 1) { return 1; }
        return fib_norm(n-1) + fib_norm(n-2);
    }
    function fib_tail(n, a, b) {
        if (typeof(a) == "undefined") a = 0;
        if (typeof(b) == "undefined") b = 1;
        return (n === 0)? a : fib_tail(n - 1, b, a + b);
    }
    logs('test').record("fib_tail_main start");
    fib_tail_main(20);
    logs('test').record("fib_tail_main end");
    logs('test').record("fib_norm_main start");
    fib_norm_main(20);
    logs('test').record("fib_norm_main end");
});
/// =================================================================
TEST_CASE('test_stub', function() {
    var s = stub();
    s.add(511677.928, 3840348.867);
    s.add(511675.876, 3840328.972);
    s.add(511673.741, 3840309.086);
    s.add(511671.523, 3840289.210);
    s.add(511669.223, 3840269.343);
    s.dump();
    var r;
    r = s.where(511675.876, 3840328.972);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
    r = s.where(511677.928, 3840348.867);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
    r = s.where(511673.741, 3840309.086);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
    r = s.where(511671.523, 3840289.210);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
    r = s.where(511669.223, 3840269.343);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
    r = s.where(511675.876, 3840328.972, 3);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
    r = s.where(511553.741, 3840309.086, 2);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
});
/// =================================================================
TEST_CASE('test_stub_zz3', function() {
    load('stub_zz3.js');
    logs('stub').console(false);
    var s = stub();
    stub_zz3(s).dump();
    var r;
    r = s.where(511120.074000,3834094.333000);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
    r = s.where(511120.113000,3834094.413000);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
    r = s.where(511669.029000,3841475.550000);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
    r = s.where(511669.062000,3841475.344000);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
    r = s.where(511721.730000,3840840.780000);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
    r = s.where(511236.577000,3834733.627000);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
    r = s.where(511236.577000,3834733.627000, 363);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
});
/// =================================================================
TEST_CASE('test_range', function() {
    var s = stub();
    s.add(511677.928, 3840348.867);
    s.add(511675.876, 3840328.972);
    s.add(511673.741, 3840309.086);
    s.dump();
    var r = range();
    r.update(s.lines(0).line, area.road_width/2, area.road_width/2);
    logs('test').record("stub0 range: " + r.toStringSvg());
});
/// =================================================================
TEST_CASE('test_stub_svg', function() {
    load('stub_zz3.js');
    load('stub_svg.js');
    var s = stub();
    stub_zz3(s);
    stub_svg(s).scale(10).toSvg();
    var r;
    r = s.where(510857.834,3844895.164);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
});
/// =================================================================
TEST_CASE('test_stub_zz4', function() {
    load('stub_zz4.js');
    logs('stub').console(false);
    var s = stub();
    stub_zz4(s).dump();
    var r;
    r = s.where(503711.866,3817090.341);
    logs('test').record((!r)? "null" : (r.index + ", " + r.coor));
});
