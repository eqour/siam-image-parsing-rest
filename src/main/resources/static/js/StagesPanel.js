class StagesPanel {
    constructor(props) {
        this.index = -1;
        this.stagesIcons = props.stages;
        this.onNextStage = $.Callbacks();
        this.onPrevStage = $.Callbacks();
        this.nextStage();
    }

    nextStage() {
        if (this.index >= 0) {
            const currentStage = $(this.stagesIcons[this.index]);
            currentStage.removeClass('item-active');
        }
        this.index++;
        const nextStage = $(this.stagesIcons[this.index]);
        nextStage.removeClass('item-muted');
        nextStage.addClass('item-active');
        this.onNextStage.fire({id: this.index});
    }

    prevStage() {
        if (this.index == 0) return;
        const currentStage = $(this.stagesIcons[this.index]);
        currentStage.removeClass('item-active');
        currentStage.addClass('item-muted');
        this.index--;
        const prevStage = $(this.stagesIcons[this.index]);
        prevStage.addClass('item-active');
        this.onPrevStage.fire({id: this.index});
    }
    
}

export default StagesPanel;
