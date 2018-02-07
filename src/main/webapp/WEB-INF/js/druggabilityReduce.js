function Reduce(key, values) {
	var size = values.length;
	var result = (Array.sum(values)) / size;
	return result;
}