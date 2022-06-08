class StagesPanel {
    constructor() {
        this.index = -1;
        this.stagesIcons = document.querySelectorAll('.stages-panel .item');
        this.onNextStage = $.Callbacks();
        this.onPrevStage = $.Callbacks();
        this.nextStage();
    }

    setStage(index) {
        if (this.index !== index) {
            if (this.index !== -1) this.stagesIcons[this.index].classList.remove('item-active');
            this.stagesIcons[index].classList.add('item-active');
            for (let i = 0; i <= index; i++) {
                this.stagesIcons[i].classList.remove('item-muted');
            }
            for (let i = index + 1; i < this.stagesIcons.length; i++) {
                this.stagesIcons[i].classList.add('item-muted');
            }
            this.index = index;
        }
    }

    nextStage() {
        this.setStage(this.index + 1);
        this.onNextStage.fire({id: this.index});
    }

    prevStage() {
        this.setStage(this.index - 1);
        this.onPrevStage.fire({id: this.index});
    }
    
}

export default StagesPanel;
