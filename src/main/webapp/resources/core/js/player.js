
(function ($) {

    $.fn.player = function (options) {
        var defaults = {
            keys: [],
            stepForwardBtn: $(this).find(".player-step-forward").removeClass("disabled").unbind("click"),
            stepBackwardBtn: $(this).find(".player-step-backward").addClass("disabled").unbind("click"),
            fastForwardBtn: $(this).find(".player-fast-forward").removeClass("disabled").unbind("click"),
            fastBackwardBtn: $(this).find(".player-fast-backward").addClass("disabled").unbind("click"),
            steps: $(this).find(".player-steps").unbind("change"),
            //playBtn: $(this).find(".player-play"),
            step: function (data) {}
        };
        var opts = $.extend({}, defaults, options);
        var data = {position: 0};

        function callStep(step) {
            data.position = Math.min(Math.max(0, step), opts.keys.length - 1);
            opts.steps.val(opts.keys[data.position]);
            if ($.isFunction(opts.step)) {
                opts.step.call(this, data);
            }
            opts.stepBackwardBtn.removeClass("disabled");
            opts.fastBackwardBtn.removeClass("disabled");
            opts.stepForwardBtn.removeClass("disabled");
            opts.fastForwardBtn.removeClass("disabled");
            if (data.position === 0) {
                opts.stepBackwardBtn.addClass("disabled");
                opts.fastBackwardBtn.addClass("disabled");
            }
            if (data.position === opts.keys.length - 1) {
                opts.stepForwardBtn.addClass("disabled");
                opts.fastForwardBtn.addClass("disabled");
            }
        }
        
        if ($(opts.steps).children().length === 0) {
            $.each(opts.keys, function () {
                $("<option />").val(this).text(this).appendTo(opts.steps);
            });
        }
        if (opts.keys.length !== 0) {
            data.position = 0;
            opts.steps.val(opts.keys[0]);
        }
        
        opts.steps.on("change", function (e) {
            callStep(e.target.selectedIndex);
        });
        //register buttons events       
        opts.stepForwardBtn.on("click", function () {
            if (!$(this).hasClass("disabled")) {
                callStep(data.position + 1);
            }
        });
        opts.stepBackwardBtn.on("click", function () {
            if (!$(this).hasClass("disabled")) {
                callStep(data.position - 1);
            }
        });
        opts.fastForwardBtn.on("click", function () {
            if (!$(this).hasClass("disabled")) {
                callStep(opts.keys.length - 1);
            }
        });
        opts.fastBackwardBtn.on("click", function () {
            if (!$(this).hasClass("disabled")) {
                callStep(0);
            }
        });
        return this;
    };

})(jQuery);
