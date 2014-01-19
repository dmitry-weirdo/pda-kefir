Ext.namespace('Kefir.vtype');

Kefir.vtype.INN_PHYSICAL_LENGTH = 12;
Kefir.vtype.INN_JURICAL_LENGTH = 10;
Kefir.vtype.INN_PHYSICAL_MULTIPLIERS_DIGIT_11 = [ 7, 2, 4, 10, 3, 5, 9, 4, 6, 8 ];
Kefir.vtype.INN_PHYSICAL_MULTIPLIERS_DIGIT_12 = [ 3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8 ];
Kefir.vtype.INN_JURIDICAL_MULTIPLIERS_DIGIT_10 = [ 2, 4, 10, 3, 5, 9, 4, 6, 8 ];

Kefir.vtype.OGRN_LENGTH = 13;
Kefir.vtype.KPP_LENGTH = 9;
Kefir.vtype.BIK_LENGTH = 9;

Kefir.vtype.isInnChecksumCorrect = function(digits, multipliers, checksumIndex) {
	var result = 0;
	for (var i = 0; i < multipliers.length; i++)
		result += digits[i] * multipliers[i];

	return digits[checksumIndex] == (result % 11 % 10);
};

Ext.apply(Ext.form.VTypes, {
	num: function(v) {
		if (!v)
			return true; // allow empty string

		return /[\d]/i.test(v);
	},
	numText: 'Необходимо ввести целое положительное числовое значение',
	numMask: /[\d]/i
});

Ext.apply(Ext.form.VTypes, {
	dateField: function(v) {
		if (!v)
			return true; // allow empty string

		return /^\d{2}.\d{2}.\d{4}$/i.test(v);
	},
	dateFieldText: 'Необходимо дату в формате "ДД.ММ.ГГГГ"',
	dateFieldMask: /[\d.]/i
});

Ext.apply(Ext.form.VTypes, {
	positiveNum: function(v) {
		if (!v)
			return true; // allow empty string

		if (!/^\d+$/.test(v))
			return false;

		if (!parseInt(v))
			return false; // disallow 0 value

		return true;
	},
	positiveNumText: 'Необходимо ввести целое положительное числовое значение',
	positiveNumMask: /[\d]/i
});

Ext.apply(Ext.form.VTypes, {
	rus: function(v) {
		if (!v)
			return true; // allow empty string

		return /[a-яА-ЯёЁ. ]/i.test(v);
	},
	rusText: 'Необходимо ввести русские символы от А до Я, Ё или точку',
	rusMask: /[а-яА-ЯёЁ. ]/i
});

Ext.apply(Ext.form.VTypes, {
	rusName: function(v) {
		if (!v)
			return true; // allow empty string

		return /[a-яА-ЯёЁ]/i.test(v);
	},
	rusNameText: 'Необходимо ввести русские символы от А до Я или Ё',
	rusNameMask: /[а-яА-ЯёЁ]/i
});

Ext.apply(Ext.form.VTypes, {
	rusAndNumbers: function(v) {
		if (!v)
			return true; // allow empty string

		return /[^a-zA-Z]/i.test(v);
	},
	rusAndNumbersText: 'Необходимо ввести русские символы от А до Я, Ё, цифры или знаки препинания',
	rusAndNumbersMask: /[^a-zA-Z]/i
});

Ext.apply(Ext.form.VTypes, {
	number: function(v) {
		if (!v)
			return true; // allow empty string

		return /^[\d]+[,]{0,1}[\d]{0,2}$/i.test(v);
	},
	numberText: 'Необходимо ввести числовое значение с не более, чем 2 знаками после запятой',
	numberMask: /[\d,]/i
});

Ext.apply(Ext.form.VTypes, {
	innPhysical: function(v) {
		if (!v)
			return true; // allow empty string

		if  (!/^[\d]{12}$/i.test(v))
		{
			this.innPhysicalText = 'Необходимо ввести числовое значение из 12 цифр';
			return false;
		}

		var digits = Kefir.parseStringToIntArray(v);
		if (
			Kefir.vtype.isInnChecksumCorrect(digits, Kefir.vtype.INN_PHYSICAL_MULTIPLIERS_DIGIT_11, 10)
				&&
				Kefir.vtype.isInnChecksumCorrect(digits, Kefir.vtype.INN_PHYSICAL_MULTIPLIERS_DIGIT_12, 11)
			)
			return true;

		this.innPhysicalText = 'Контрольная сумма ИНН неверна';
		return false;
	},
	innPhysicalText: 'Необходимо ввести числовое значение из 12 цифр',
	innPhysicalMask: /[\d]/i
});

Ext.apply(Ext.form.VTypes, {
	innJuridical: function(v) {
		if (!v)
			return true; // allow empty string

		if  (!/^[\d]{10}$/i.test(v))
		{
			this.innJuridicalText = 'Необходимо ввести числовое значение из 10 цифр';
			return false;
		}

		var digits = Kefir.parseStringToIntArray(v);
		if ( Kefir.vtype.isInnChecksumCorrect(digits, Kefir.vtype.INN_JURIDICAL_MULTIPLIERS_DIGIT_10, 9) )
			return true;

		this.innJuridicalText = 'Контрольная сумма ИНН неверна';
		return false;
	},
	innJuridicalText: 'Необходимо ввести числовое значение из 10 цифр',
	innJuridicalMask: /[\d]/i
});

Ext.apply(Ext.form.VTypes, {
	ogrn: function(v) {
		if (!v)
			return true; // allow empty string

		if (!/^[\d]{13}$/i.test(v))
		{
			this.ogrnText = 'Необходимо ввести числовое значение из 13 цифр';
			return false;
		}

		var value = parseInt(v.substring(0, 12));
		var checksum = parseInt(v.substring(12, 13));

		if ( (value % 11 % 10) == checksum )
			return true;

		this.ogrnText = 'Контрольная сумма ОГРН неверна';
		return false;
	},
	ogrnText: 'Необходимо ввести числовое значение из 13 цифр',
	ogrnMask: /[\d]/i
});

Ext.apply(Ext.form.VTypes, {
	imns: function(v) {
		if (!v)
			return true; // allow empty string

		return /[\d]{4}/i.test(v);
	},
	imnsText: 'Необходимо ввести числовое значение из 4 цифр',
	imnsMask: /[\d]/i
});

Ext.apply(Ext.form.VTypes, {
	kpp: function(v) {
		if (!v)
			return true; // allow empty string

		return /[\d]{9}/i.test(v);
	},
	kppText: 'Необходимо ввести числовое значение из 9 цифр',
	kppMask: /[\d]/i
});
Ext.apply(Ext.form.VTypes, {
	bik: function(v) {
		if (!v)
			return true; // allow empty string

		return /[\d]{9}/i.test(v);
	},
	bikText: 'Необходимо ввести числовое значение из 9 цифр',
	bikMask: /[\d]/i
});

Ext.apply(Ext.form.VTypes, {
	zipCode: function(v) {
		if (!v)
			return true; // allow empty string

		return /^\d{6}$/i.test(v);
	},
	zipCodeText: 'Необходимо ввести значение из 6 цифр',
	zipCodeMask: /\d/i
});

// ФИО физического лица
Ext.apply(Ext.form.VTypes, {
	physicalName: function(v) {
		if (!v)
			return true; // allow empty string

		if (!v.trim())
			return false;

		return /^[а-яА-ЯёЁ\s\-]+$/i.test(v);
	},
	physicalNameText: 'Необходимо ввести значение, которое может содержать русские буквы, пробел и дефис',
	physicalNameMask: /[а-яА-ЯёЁ\s\-]/i
});

Ext.apply(Ext.form.VTypes, {
	phone: function(v) {
		if (!v.trim())
			return true; // allow empty string

		return /^[\d\-\+\s\(\)]*$/i.test(v);
	},
	phoneText: 'Необходимо ввести значение, которое может содержать цифры, пробелы и знаки +, -, (, )',
	phoneMask: /[\d\-\+\s\(\)]/i
});

// адреса
Ext.apply(Ext.form.VTypes, {
	subject: function(v) {
		if (!v.trim())
			return true; // allow empty string

		return /^[а-яА-ЯёЁ\-\s\/\.]*$/i.test(v);
	},
	subjectText: 'Необходимо ввести значение, которое может содержать русские буквы, пробелы и знаки -, ., /',
	subjectMask: /[а-яА-ЯёЁ\-\s\/\.]/i // цифр нет, латинской N нет
});

Ext.apply(Ext.form.VTypes, {
	district: function(v) {
		if (!v.trim())
			return true; // allow empty string

		return /^[а-яА-ЯёЁ\-\s\/\.]*$/i.test(v);
	},
	districtText: 'Необходимо ввести значение, которое может содержать русские буквы, пробелы и знаки -, ., /',
	districtMask: /[а-яА-ЯёЁ\-\s\/\.]/i // цифр нет, латинской N нет
});

Ext.apply(Ext.form.VTypes, {
	city: function(v) {
		if (!v.trim())
			return true; // allow empty string

		return /^[а-яА-ЯёЁ\d\-\s\/\.]*$/i.test(v);
	},
	cityText: 'Необходимо ввести значение, которое может содержать русские буквы, цифры, пробелы и знаки -, ., /',
	cityMask: /[а-яА-ЯёЁ\d\-\s\/\.]/i // латинской N нет
});

Ext.apply(Ext.form.VTypes, {
	cityDistrict: function(v) {
		if (!v.trim())
			return true; // allow empty string

		return /^[а-яА-ЯёЁ\d\-\s\/\.]*$/i.test(v);
	},
	cityDistrictText: 'Необходимо ввести значение, которое может содержать русские буквы, цифры, пробелы и знаки -, ., /',
	cityDistrictMask: /[а-яА-ЯёЁ\d\-\s\/\.]/i // латинской N нет
});

Ext.apply(Ext.form.VTypes, {
	locality: function(v) {
		if (!v.trim())
			return true; // allow empty string

		return /^[а-яА-ЯёЁnN\d\-\s\/\(\)\.\_]*$/i.test(v);
	},
	localityText: 'Необходимо ввести значение, которое может содержать русские буквы, цифры, скобки, пробелы и знаки -, ., /, N, _',
	localityMask: /[а-яА-ЯёЁnN\d\-\s\/\(\)\.\_]/i
});

Ext.apply(Ext.form.VTypes, {
	street: function(v) {
		if (!v.trim())
			return true; // allow empty string

		return /^[а-яА-ЯёЁnN\d\-\s\/\(\)\.]*$/i.test(v);
	},
	streetText: 'Необходимо ввести значение, которое может содержать русские буквы, цифры, скобки, пробелы и знаки -, ., /, N',
	streetMask: /[а-яА-ЯёЁnN\d\-\s\/\(\)\.]/i
});

Ext.apply(Ext.form.VTypes, {
	house: function(v) {
		if (!v.trim())
			return true; // allow empty string

		return /^[а-яА-ЯёЁ\d\/]*$/i.test(v);
	},
	houseText: 'Необходимо ввести значение, которое может содержать цифры, русские буквы, пробелы и знак /',
	houseMask: /[а-яА-ЯёЁ\d\/]/i
});

Ext.apply(Ext.form.VTypes, {
	building: function(v) {
		if (!v.trim())
			return true; // allow empty string

		return /^[а-яА-ЯёЁ\d\.]*$/i.test(v);
	},
	buildingText: 'Необходимо ввести значение, которое может содержать цифры, русские буквы и знак .',
	buildingMask: /[а-яА-ЯёЁ\d\.]/i
});

Ext.apply(Ext.form.VTypes, {
	apartment: function(v) {
		if (!v.trim())
			return true; // allow empty string

		return /^[а-яА-ЯёЁ\d\s\.]*$/i.test(v);
	},
	apartmentText: 'Необходимо ввести значение, которое может содержать цифры, точку, пробелы и русские буквы',
	apartmentMask: /[а-яА-ЯёЁ\d\s\.]/i
});

Ext.apply(Ext.form.VTypes, {
	rfPassport: function(v) {
		if (!v.trim() || v == '__ __ ______')
			return true; // allow empty string

		return /^\d{2}\s\d{2}\s\d{6}$/i.test(v);
	},
	rfPassportText: 'Необходимо ввести значение в формате "12 34 567890"',
	rfPassportMask: /\d/i
});

Ext.apply(Ext.form.VTypes, {
	identityCardNumber: function(v) {
		if (!v.trim())
			return true; // allow empty string

		return /^[a-zA-Zа-яА-ЯёЁ\d\s\-]*$/i.test(v);
	},
	identityCardNumberText: 'Необходимо ввести значение, которое может содержать цифры, буквы, знак \'-\' и пробел',
	identityCardNumberMask: /[a-zA-Zа-яА-ЯёЁ\d\s\-]/i
});

Ext.apply(Ext.form.VTypes, {
	identityCardIssuer: function(v) {
		if (!v.trim())
			return true; // allow empty string

		return /^[^\,]*$/i.test(v);
	},
	identityCardIssuerText: 'Значение не может содержать запятые',
	identityCardIssuerMask: /[^\,]/i
});