class TextHelper {
    static formatDoubleToString(value) {
        let ans;
        if (Number.isNaN(value) || Number.POSITIVE_INFINITY === value || Number.NEGATIVE_INFINITY === value || value === undefined) {
            return value;
        }
        if ((Math.abs(value) >= 0.001 & Math.abs(value) <= 1000) || value === 0) {
            ans = Number(value.toFixed(3));
        } else {
            ans = parseFloat(value.toExponential(3)).toExponential();
            ans = ans.replace('+', '');
        }
        return ans;
    }
}

export default TextHelper