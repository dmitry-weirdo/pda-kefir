Ext.namespace('Kefir.mainMenu');

Kefir.mainMenu.MAIN_MENU_ID = 'mainMenu';

Kefir.mainMenu.MainMenu = function() {
	var title;
	var buttons;
	var toolbar;

	function getMainMenuPanel() {
		return new Ext.Panel({
			id: Kefir.mainMenu.MAIN_MENU_ID,
			layout: 'vbox',
			tbar: toolbar,
			title: title,
			items: buttons
		});
	}

	function initViewPort() {
		Kefir.viewPort = new Ext.Viewport({
			layout: 'fit',
			autoScroll: true,
			forceFit: true,
			hideMode: "offsets",
			defaults: { autoScroll: true },

			items: [ getMainMenuPanel() ]
		});

		Ext.DomHelper.applyStyles(Ext.getBody().dom.parentNode, { overflowY: 'visible' }); // при вылезании окна за пределы высоты браузера появляется скролл
	}
	function showViewPort() {
		initViewPort();
		Kefir.viewPort.show();
	}

	return {
		getMainMenuButton: function(config) {
			return new Ext.Button({
				id: config.id,
				cls: 'mainMenuBtn',
				text: config.text,
				tooltip: config.tooltip || config.text,
				width: config.width,
				handler: config.handler
			});
		},
		
		init: function(config) {
			title = config.title || '';
			buttons = config.buttons || [];
			toolbar = config.toolbar || [ '->' ];

			showViewPort();
		}
	}
}();