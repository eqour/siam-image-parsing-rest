const AXIS_LINEAR = 'linear';
const AXIS_LOG = 'log';

class State {
    constructor() {
        this.stage = 0;
        this.image = null;
        this.transform = State.createDefaultTransform();
        this.corrections = {image: null};
        this.selection = State.createDefaultSelection();
        this.axes = State.createDefaultAxes();
        this.parsing = State.createDefaultParsing();
    }

    static createDefaultAxes() {
        return {
            x: this.createDefaultAxis(),
            y: this.createDefaultAxis()
        }
    }

    static createDefaultParsing() {
        return {
            color: null,
            colorDifference: 20,
            points: []
        }
    }

    static createDefaultSelection() {
        return {pixels: [], pencil: 10, eraser: 10}
    }

    static createDefaultAxis() {
        return {
            type: AXIS_LINEAR,
            start: [],
            end: [],
            enableUnits: false,
            unitType: null,
            unit: null
        }
    }

    static createDefaultTransform() {
        return {rotation: 0, flipX: false, flipY: false, rectangle: this.createCorners(), perspective: this.createCorners()}
    }

    static createCorners() {
        let x = 0.5;
        let y = 0.5;
        let delta = 0.2;
        return [[x, y], [x, y + delta], [x + delta, y + delta], [x + delta, y]];
    }
}

export default State