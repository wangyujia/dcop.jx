/// logs.js


var logs = (function() {
    var MAX_LENGTH = 5*1024*1024;
    var ZIP_BUFFER = 16*1024;
    var m_path = "./log";
    var m_nodes = {};

    var stk = function() {
        var s = getCallStack();
        // print('stack: ' + s.join('\n'));
        return '<(' + s[2] + ')<-(' + s[3] + ')>';
    }

    var log = function(type, path) {
        var m_console = true;
        var o = {
            type: type,
            path: path,
            start: false
        };

        add_obj_method(o, 'console', function() {
            return m_console;
        });
    
        add_obj_method(o, 'console', function(bool) {
            m_console = bool;
        });
    
        add_obj_method(o, 'write', function(info) {
            var File = Java.type('java.io.File');
            var FileOutputStream = Java.type('java.io.FileOutputStream');
            var OutputStreamWriter = Java.type('java.io.OutputStreamWriter');
            var file = new File(this.path);
            var stream = new FileOutputStream(file, true);
            var writer = new OutputStreamWriter(stream);
            writer.append(info);
            writer.close();
            stream.close();
            if (file.length() > MAX_LENGTH) {
                this.backup();
            }
        });
    
        add_obj_method(o, 'backup', function() {
            var SimpleDateFormat = Java.type('java.text.SimpleDateFormat');
            var Date = Java.type('java.util.Date');
            var File = Java.type('java.io.File');
            var FileInputStream = Java.type('java.io.FileInputStream');
            var FileOutputStream = Java.type('java.io.FileOutputStream');
            var GZIPOutputStream = Java.type('java.util.zip.GZIPOutputStream');
            var Byte = Java.type('byte[]');
            var df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            var date = df.format(new Date());
            var file = new File(this.path);
            var fis = new FileInputStream(file);
            var fos = new FileOutputStream(this.path + '.' + date + '.gz', false);
            var gos = new GZIPOutputStream(fos);
            var count;
            var data = new Byte(ZIP_BUFFER);
            while ((count = fis.read(data, 0, ZIP_BUFFER)) != -1) {
                gos.write(data, 0, count);
            }
            gos.finish();
            gos.flush();
            gos.close();
            fis.close();
            fos.flush();
            fos.close();
            file.delete();
        });

        add_obj_method(o, 'record', function(info) {
            if (typeof(info) != "undefined") info = info.toString();
            if (!info.length) return this;
            var SimpleDateFormat = Java.type('java.text.SimpleDateFormat');
            var Date = Java.type('java.util.Date');
            var df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            var date = df.format(new Date());
            if (!this.start) {
                this.write("\r\n[" + date + "] ================== " + this.type + " start ================== \r\n");
                if (m_console) print("[" + date + "] ================== " + this.type + " start ================== ");
                this.start = true;
            }
            var s = '[' + date + '] ' + info; //  + ' ' + stk();
            this.write(s + '\r\n');
            if (m_console) print(s);
        });

        return o;
    };

    var o = {};

    add_obj_method(o, 'path', function() {
        return m_path;
    });

    add_obj_method(o, 'path', function(str) {
        m_path = str;
        var File = Java.type('java.io.File');
        var file = new File(m_path);
        file.mkdirs();
    });

    add_obj_method(o, 'maxLength', function() {
        return MAX_LENGTH;
    });

    add_obj_method(o, 'maxLength', function(len) {
        MAX_LENGTH = len;
    });

    add_obj_method(o, 'zipBuffer', function() {
        return ZIP_BUFFER;
    });

    add_obj_method(o, 'zipBuffer', function(len) {
        ZIP_BUFFER = len;
    });


    return function(type) {
        if (typeof(type) == "undefined") {
            return o;
        }

        var node = m_nodes[type];
        if (!node) {
            node = log(type, m_path + '/' + type + '.log');
            m_nodes[type] = node;
            o.path(m_path);
        }

        return node;
    }
})();
