
var vue = new Vue({

    el: '#app',

    data: {

        message: "",
        messages: [],
        withEnter: true,
        nbPseudo: 10,
        pseudos: []

    },

    methods: {

        generatePseudo: function () {
            var n = this.nbPseudo;
            if(!isNaN(parseFloat(n)) && isFinite(n)) {
                var v = $.ajax({
                    type: "GET",
                    url: "/generate/pseudo/nb=" + n + "&syll=2",
                    async: false
                }).responseText;

                var json = JSON.parse(v);
                console.log(json);
                var index = 0;
                this.pseudos = [];
                while (json[index] != null)
                {
                    console.log(json[index]);
                    this.pseudos.push({
                        i: index,
                        get: json[index]
                    });
                    index++;
                }
            }
        },

        actualize: function () {

            var v = $.ajax({
                type: "GET",
                url: "/chat/all",
                async: false
            }).responseText;

            this.jsoned = JSON.parse(v);
            var j = this.jsoned;
            var i = 0;

            this.messages = [];
            while (j[i] != null) {
                this.messages.push({
                    author: j[i].author,
                    content: j[i].content,
                    id:  j[i].id,
                    date: j[i].date,
                    imageLoc: "/assets/" + j[i].imageLoc
                });
                i++;
            }

        },

        sendMessage: function () {

            var message = this.message;
            if (message.length == 0)
                return;

            $.post(
                'chat/submit',
                {
                    message: message
                }
            );

            this.message = "";
            var self = this;

            setTimeout(function () {
                self.actualize()
            }, 500)

        }
    }
});

window.onload = function () {
    window.setInterval(vue.actualize, 2000);
};

