class lol {

}

new Vue({

    el: "#code",

    data: {
        code: "",
        error: false,
        ok: false,
        s: []
    },

    methods: {
        checkCode: () => {
            var self = this;
            $.post(
                '/javascript/code_verification',
                {
                    code: this.code
                },
                function(d) {
                    self.s = [];
                    var data = JSON.parse(d);
                    self.error = !data.success;
                    data.message.split("{n}").forEach((e) => {
                        if(e.length != 0)
                            self.s.push(e);
                    });
                }
            );
            this.ok = true;
        }
    }
});
