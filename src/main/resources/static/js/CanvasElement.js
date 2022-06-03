class CanvasElement {
    constructor(props) {
        this.x = props.x;
        this.y = props.y;
        this.ctx = props.ctx;
        this.onPositionChanged = $.Callbacks();
    }

    isIncluded(x, y) {
        return false;
    }

    render() {
    }

    getTarget(x, y) {
        return this.isIncluded(x, y) ? this : null;
    }

    setPosition(x, y, fireEvent = true) {
        this.x = x;
        this.y = y;
        if (fireEvent) {
            this.onPositionChanged.fire({target: this, x: this.x, y: this.y});
        }
    }
}

export default CanvasElement;
