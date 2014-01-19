Ext.namespace('Kefir');

Array.prototype.contains = function(value){
	return this.indexOf(value) >= 0;
}

Kefir.StringUtils = function() {
	return {
		replaceChar: function(str, charPos, newChar) {
			return str.substr(0, charPos) + newChar + str.substr(charPos + 1)
		},

		isDigit: function(charCode) {
			return (
				(charCode >= 48 && charCode <= 57) // digits
				||
				(charCode >= 96 && charCode <= 105) // numpad digits
			);
		},
		getDigit: function(charCode) {
			if (charCode >= 48 && charCode <= 57)
				return String.fromCharCode(charCode);

			if (charCode >= 96 && charCode <= 105)
				return String.fromCharCode(charCode - 48);

			alert('Incorrect digit char code!');
		},
		
		isLetter: function(charCode, allowRus) {
			// проверка по совпадению с латиницей: АВЕКМНОРСТУХ
			switch(charCode)
			{
				// русские буквы, совпадащие с латиницей -> валидны всегда
				case 70:  // f -> а
				case 68:  // d -> в
				case 84:  // t -> е
				case 82:  // r -> к
				case 86:  // v -> м
				case 89:  // y -> н
				case 74:  // j -> о
				case 72:  // h -> р
				case 67:  // c -> с
				case 78:  // n -> т
				case 69:  // e -> у
				case 219: // [ -> х
					return true;

				// русские буквы, не совпадающие с латиницей -> валидны только когда разрешен ввод русских букв
				case 65: // a -> Ф
				case 66: // b -> и
				case 71: // g -> п
				case 73: // i -> ш
				case 75: // k -> л
				case 76: // l -> д
				case 77: // m -> ь
				case 79: // o -> щ
				case 80: // p -> з
				case 81: // q -> й
				case 83: // s -> ы
				case 85: // u -> г
				case 87: // w -> ц
				case 88: // x -> ч
				case 90: // z -> я

				case 221: // ] -> ъ
				case 59:  // ; -> ж
				case 222: // ' -> э
				case 188: // , -> б
				case 190: // . -> ю
//				case 192: // ` -> ё // ё запрещена в любом случае
					return allowRus;

				default:
					return false;
			}
//			return (
//				(charCode >= 65 && charCode <= 90) || // english letters
//				charCode == 219 || // [  х
//				charCode == 221 || // ]  ъ
//				charCode == 59 || // ;  ж
//				charCode == 222 || // '  э
//				charCode == 188 || // ,  б
//				charCode == 190 || // .  ю
//				charCode == 192    // `  ё
//			)
		},
		getLetter: function(charCode) {
			switch (charCode)
			{
				case 65: return '\u0424'; // a -> Ф
				case 66: return '\u0418'; // b -> и
				case 67: return '\u0421'; // c -> с
				case 68: return '\u0412'; // d -> в
				case 69: return '\u0423'; // e -> у
				case 70: return '\u0410'; // f -> а
				case 71: return '\u041f'; // g -> п
				case 72: return '\u0420'; // h -> р
				case 73: return '\u0428'; // i -> ш
				case 74: return '\u041E'; // j -> о
				case 75: return '\u041B'; // k -> л
				case 76: return '\u0414'; // l -> д
				case 77: return '\u042C'; // m -> ь
				case 78: return '\u0422'; // n -> т
				case 79: return '\u0429'; // o -> щ
				case 80: return '\u0417'; // p -> з
				case 81: return '\u0419'; // q -> й
				case 82: return '\u041A'; // r -> к
				case 83: return '\u042B'; // s -> ы
				case 84: return '\u0415'; // t -> е
				case 85: return '\u0413'; // u -> г
				case 86: return '\u041C'; // v -> м
				case 87: return '\u0426'; // w -> ц
				case 88: return '\u0427'; // x -> ч
				case 89: return '\u041D'; // y -> н
				case 90: return '\u042F'; // z -> я

				case 219: return '\u0425'; // [ -> х
				case 221: return '\u042A'; // ] -> ъ
				case 59:  return '\u0416'; // ; -> ж
				case 222: return '\u042D'; // ' -> э
				case 188: return '\u0411'; // , -> б
				case 190: return '\u042E'; // . -> ю
				case 192: return '\u0401'; // ` -> ё
			}
		},

		isCategoryLetter: function(charCode) {
			return (charCode >= 65 && charCode <= 70); // A, B, C, D, E, F
		},

		capitalize: function(str) {
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
	}
}();