Ext.ns('su.opencode.kefir.sampleSrv.mainMenu');

su.opencode.kefir.sampleSrv.mainMenu.MainMenu = function() {
	var TITLE = 'Test Kefir App';
	var BUTTON_WIDTH = 400;

	var COMBO_BOX_ENTITYS_LIST_BUTTON_ID = 'comboBoxEntity-list-mainMenu';
	var COMBO_BOX_ENTITYS_LIST_BUTTON_TEXT = 'ComboBoxEntities list';

	var CHOOSE_ENTITYS_LIST_BUTTON_ID = 'chooseEntity-list-mainMenu';
	var CHOOSE_ENTITYS_LIST_BUTTON_TEXT = 'ChooseEntities list';

	var TEST_ENTITIES_LIST_BUTTON_ID = 'testEntity-list-mainMenu';
	var TEST_ENTITIES_LIST_BUTTON_TEXT = 'Список тестовых сущностей';
	var TEST_ENTITIES_LIST_BUTTON_TOOL_TIP = 'Тестовая сущность связана с комбо сущностью и со связанной сущностью';

	// ${APPEND_BUTTON_CONSTANTS}

	var buttons = new Array();

	function addButton(button) {
		buttons.push(button);
	}
	function getMainMenuButton(id, text, tooltip, handler) {
		return Kefir.mainMenu.MainMenu.getMainMenuButton({
			id: id,
			text: text,
			tooltip: tooltip,
			width: BUTTON_WIDTH,
			handler:  handler
		});
	}
	function addMainMenuButton(id, text, tooltip, handler) {
		addButton( getMainMenuButton(id, text, tooltip, handler) );
	}

	function getButtons() {
		buttons.length = 0;

		addMainMenuButton(COMBO_BOX_ENTITYS_LIST_BUTTON_ID, COMBO_BOX_ENTITYS_LIST_BUTTON_TEXT, null, function() {
			su.opencode.kefir.sampleSrv.comboBoxEntity.ComboBoxEntitysList.init({});
		});

		addMainMenuButton(CHOOSE_ENTITYS_LIST_BUTTON_ID, CHOOSE_ENTITYS_LIST_BUTTON_TEXT, null, function() {
			su.opencode.kefir.sampleSrv.chooseEntity.ChooseEntitysList.init({});
		});

		addMainMenuButton(TEST_ENTITIES_LIST_BUTTON_ID, TEST_ENTITIES_LIST_BUTTON_TEXT, TEST_ENTITIES_LIST_BUTTON_TOOL_TIP, function() {
			su.opencode.kefir.sampleSrv.testEntity.TestEntitysList.init({});
		});

		// ${APPEND_BUTTON_VARIABLE}

		return buttons;
	}

	return {
		init: function() {
			Kefir.mainMenu.MainMenu.init({
				title: TITLE,
				buttons: getButtons()
			});
		}
	}
}();