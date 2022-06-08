import ParsingAPI from "./ParsingAPI.js";
import FileHelper from "./FileHelper.js";

const IMAGE_DATA_PREFIX = 'data:image/png;base64,';

class ImageHelper {

    static prepareRectanglePoints(points) {
        let minX = points[0][0];
        let maxX = points[0][0];
        let minY = points[0][1];
        let maxY = points[0][1];
        for (let i = 1; i < points.length; i++) {
            minX = Math.min(points[i][0], minX);
            maxX = Math.max(points[i][0], maxX);
            minY = Math.min(points[i][1], minY);
            maxY = Math.max(points[i][1], maxY);
        }
        return {x: minX, y: minY, w: maxX - minX, h: maxY - minY}
    }

    static cutImage(img, x, y, w, h) {
        const tempCanvas = document.createElement('canvas');
        tempCanvas.width = w;
        tempCanvas.height = h;
        tempCanvas.getContext('2d').drawImage(img, x, y, w, h, 0, 0, w, h);
        return tempCanvas.toDataURL();
    }

    static changePerspective(img, points) {
        ParsingAPI.sendRequest('/api/perspective',
            JSON.stringify({
                points: points,
                image: FileHelper.parseDataURL(self.canvas.getImageDataURL()).data,
                outputWidth: Math.round((Math.abs(points[1][0] - points[0][0]) + Math.abs(points[2][0] - points[3][0])) / 2),
                outputHeight: Math.round((Math.abs(points[3][1] - points[0][1]) + Math.abs(points[2][1] - points[1][1])) / 2)
            })
        )
    }

}

export default ImageHelper