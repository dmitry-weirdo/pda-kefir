Ext.namespace('Kefir.render');

Kefir.render.dontShowNilRender = function(value) {
	if (!value || value == '0')
		return '';

	return value;
};
Kefir.render.dontShowNilFloatRender = function(value) {
	if (!value || value == '0')
		return '';

	return value.toFixed(2).replace('.', ',');
};
Kefir.render.floatRenderer = function(value) {
	if (!value)
		return '';

	return value.toFixed(2).replace('.', ',');
};
Kefir.render.dateRenderer = function(value) {
	if (value)
		return value.format('d.m.Y');

	return '';
};
Kefir.render.loginRenderer = function(value) {
	if (value != Kefir.render.user)
		return value;

	return '<span style=\"font-weight: bold;\">' + value + '</span>';
};
Kefir.render.booleanRenderer = function(value, element) {
	element.css += ' x-grid3-check-col-td';
	return '<div class=\"x-grid3-check-col' + (value ? '-on' : '') + '\">&#160;</div>';
};
Kefir.render.sexRenderer = function(value) {
	switch (value)
	{
		case Kefir.constant.Sex.MALE: return 'М';
		case Kefir.constant.Sex.FEMALE: return 'Ж';
		default: return '';
	}
};