const AXIS_LINEAR = 'linear';
const AXIS_LOG = 'log';

class State {
    constructor() {
        this.stage = 0;
        this.image = null;
        this.transform = State.createDefaultTransform();
        this.colors = State.createDefaultColors();
        this.selection = State.createDefaultSelection();
        this.axes = State.createDefaultAxes();
        this.parsing = State.createDefaultParsing();
    }

    setDefaultTransform() {
        this.transform.rotation = 0;
        this.transform.flipX = false;
        this.transform.flipY = false;
        let rectangle = State.createCorners();
        for (let i = 0; i < rectangle.length; i++) {
            this.transform.rectangle[i][0] = rectangle[i][0];
            this.transform.rectangle[i][1] = rectangle[i][1];
            this.transform.perspective[i][0] = rectangle[i][0];
            this.transform.perspective[i][1] = rectangle[i][1];
        }
    }

    setContrast(value) {
        this.colors.contrast = value;
    }


    setBrightness(value) {
        this.colors.brightness = value;
    }

    setSaturation(value) {
        this.colors.saturation = value;
    }

    setInvert(value) {
        this.colors.invert = value;
    }

    setPencil(value) {
        this.selection.pencil = value;
    }

    setEraser(value) {
        this.selection.eraser = value;
    }

    setDefaultColor() {
        this.colors.contrast = 100;
        this.colors.brightness = 100;
        this.colors.saturation = 100;
        this.colors.invert = false;
    }

    static createDefaultColors() {
        return {contrast: 100, brightness: 100, saturation: 100, invert:false}
    }

    static createDefaultAxes() {
        return {
            x: this.createDefaultAxis([0.2, 0.5], [0.8, 0.5]),
            y: this.createDefaultAxis([0.2, 0.2], [0.2, 0.8])
        }
    }

    static createDefaultParsing() {
        return {
            color: null,
            colorDifference: 20,
            points: [[10, 10], [50, 50], [500, 500], [510, 500], [520, 500], [530, 500]]
        }
    }

    static createDefaultSelection() {
        return {pixels: [], pencil: 101, eraser: 101}
    }

    static createDefaultAxis(start, end) {
        return {
            type: AXIS_LINEAR,
            start: start,
            end: end,
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