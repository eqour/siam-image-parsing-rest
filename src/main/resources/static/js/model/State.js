const AXIS_LINEAR = 'line';
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
        this.result = {points: []};
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

    setDefaultColors() {
        let def = State.createDefaultColors();
        this.colors.contrast = def.contrast;
        this.colors.brightness = def.brightness;
        this.colors.saturation = def.saturation;
        this.colors.invert = def.invert;
        this.colors.image = def.image;
    }

    setDefaultSelections() {
        let def = State.createDefaultSelection();
        this.selection.pencil = def.pencil;
        this.selection.eraser = def.eraser;
        this.selection.selectImage = def.selectImage;
        this.selection.resultImage = def.resultImage;
    }

    setDefaultParsing() {
        let def = State.createDefaultParsing();
        this.parsing.color = def.color;
        this.parsing.colorDifference = def.colorDifference;
        this.parsing.eraser = def.eraser;
        this.parsing.points = def.points;
    }

    setDefaultAxes() {
        this.axes = State.createDefaultAxes();
    }

    setStage(value) {
        this.stage = value;
    }

    setImage(image) {
        this.image = image;
    }

    setColoredImage(coloredImage) {
        this.colors.image = coloredImage;
    }

    setContrast(value) {
        this.colors.contrast = value;
    }

    setSelectImage(image) {
        this.selection.selectImage = image;
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

    setResultImage(value) {
        this.selection.resultImage = value;
    }

    setXAxisType(value) {
        this.axes.x.type = value;
    }

    setXAxisValueStart(value) {
        this.axes.x.value[0] = value;
    }

    setXAxisValueEnd(value) {
        this.axes.x.value[1] = value;
    }

    setYAxisType(value) {
        this.axes.y.type = value;
    }

    setYAxisValueStart(value) {
        this.axes.y.value[0] = value;
    }

    setYAxisValueEnd(value) {
        this.axes.y.value[1] = value;
    }

    setXAxisStart(value) {
        this.axes.x.start[0] = value[0];
        this.axes.x.start[1] = value[1];
    }

    setXAxisEnd(value) {
        this.axes.x.end[0] = value[0];
        this.axes.x.end[1] = value[1];
    }

    setYAxisStart(value) {
        this.axes.y.start[0] = value[0];
        this.axes.y.start[1] = value[1];
    }

    setYAxisEnd(value) {
        this.axes.y.end[0] = value[0];
        this.axes.y.end[1] = value[1];
    }

    setAxesDefaultStartEndByCanvas(canvas) {
        this.axes.x.start[0] = Math.round(canvas.width * 0.2);
        this.axes.x.start[1] = Math.round(canvas.height * 0.5);
        this.axes.x.end[0] = Math.round(canvas.width * 0.8);
        this.axes.x.end[1] = Math.round(canvas.height * 0.5);
        this.axes.y.start[0] = Math.round(canvas.width * 0.2);
        this.axes.y.start[1] = Math.round(canvas.height * 0.2);
        this.axes.y.end[0] = Math.round(canvas.width * 0.2);
        this.axes.y.end[1] = Math.round(canvas.height * 0.8);
    }

    setColorPick(value) {
        this.parsing.color = value;
    }

    setColorDifference(value) {
        this.parsing.colorDifference = value;
    }

    setPointEraser(value) {
        this.parsing.eraser = value;
    }

    setParsePoints(value) {
        this.parsing.points = value;
    }

    setResultPoints(value) {
        this.result.points = value;
    }

    setParsingPoints(points) {
        this.parsing.points = points;
    }

    setDefaultColor() {
        this.colors.contrast = 100;
        this.colors.brightness = 100;
        this.colors.saturation = 100;
        this.colors.invert = false;
    }

    static createDefaultColors() {
        return {contrast: 100, brightness: 100, saturation: 100, invert:false, image: null}
    }

    static createDefaultAxes() {
        return {
            x: this.createDefaultAxis([0, 100]),
            y: this.createDefaultAxis( [0, 100])
        }
    }

    static createDefaultParsing() {
        return {
            color: '#563d7c',
            colorDifference: 100,
            eraser: 100,
            points: []
        }
    }

    static createDefaultSelection() {
        return {pencil: 101, eraser: 101, selectImage: null, resultImage: {base64: null, id: null}};
    }

    static createDefaultAxis(value) {
        return {
            type: AXIS_LINEAR,
            start: [NaN, NaN],
            end: [NaN, NaN],
            value: value,
            enableUnits: false,
            unitType: null,
            unit: null
        }
    }

    static createDefaultTransform() {
        return {rotation: 0, flipX: false, flipY: false, rectangle: this.createCorners(), perspective: this.createCorners()}
    }

    static createCorners() {
        let x = 0.4;
        let y = 0.4;
        let delta = 0.2;
        return [[x, y], [x, y + delta], [x + delta, y + delta], [x + delta, y]];
    }
}

export default State