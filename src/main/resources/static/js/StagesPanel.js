class StagesPanel {
    constructor(props) {
        this.index = -1;
        this.stages = props.stages;
        this.nextStage();
    }

    nextStage() {
        if (this.index >= 0) {
            const currentStage = $(this.stages[this.index]);
            currentStage.removeClass('stage-active');
            currentStage.addClass('stage-passed');
        }
        this.index++;
        const nextStage = $(this.stages[this.index]);
        nextStage.addClass('stage-active');
    }
}

export default StagesPanel;
