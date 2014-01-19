Ext.namespace('Kefir.form');

Kefir.form.MAX_FILE_SIZE = 2097152;
Kefir.form.UPLOAD_URL = '/upload';
Kefir.form.ATTACHMENTS_LIST_URL = '/attachmentsList';

Kefir.form.MultiUploadPanel = Ext.extend(Mrn.petitions.MultyUploadPanel, {
	constructor: function(config) {
		Ext.apply(this, {
			url: Kefir.contextPath + Kefir.form.UPLOAD_URL,
			baseParams: config.baseParams,
			maxFileSize: config.maxFileSize || Kefir.form.MAX_FILE_SIZE,
			buttonsAt:'tbar',
			path: 'root',
			inputsIds: config.inputsIds || 'uploadFile',
			maxFiles:config.maxFiles || null,

			onAddFile:function(fu,fileName) {
				var count = this.store.getCount();
				if (!this.maxFiles || this.maxFiles > count)
				{
					Kefir.form.MultiUploadPanel.superclass.onAddFile.call(this, fu,fileName);
					this.addBtn.setDisabled(this.maxFiles == count + 1);
				}
			},

			onRemoveFile:function(record) {
				this.addBtn.enable();
				Kefir.form.MultiUploadPanel.superclass.onRemoveFile.call(this, record);
			},

			setReadOnly: function() {
				this.readOnly = true;
				this.clickRemoveText = ''; // убрать тултип "Нажать для удаления" 
				
				this.on('render', function() { // кнопки доступны только после рендера
					this.addBtn.disable();
					this.uploadBtn.disable();
					this.removeAllBtn.disable();
				});
			},

			getAttachmentsIds: function() { // получает id всех загруженных на сервер (имеющих id) аттачей в store
				var ids = '';
				this.store.each(function(record) {
					if ( Ext.isDefined(record.get('id')) )
						ids += (record.get('id') + ', ');
				});
				ids = ids.substring(0, ids.length - 2); // remove trailing ', '

				return ids;
			},

			hasUnloadedAttachments: function() { // true, если есть незагруженные на сервер аттчи, false в противном случае 
				var hasAttachmentsWithNoId = false;

				this.store.each(function(record) {
					if ( !Ext.isDefined(record.get('id')) )
						hasAttachmentsWithNoId = true;
				});

				return hasAttachmentsWithNoId;
			},

			loadAttachments: function(callback) {
				if (this.hasUnloadedAttachments())
				{
					var listenersPresent = false;
					var listeners = this.uploader.events.allfinished.listeners;
					var el = this;
					Ext.each(listeners, function(item, index, allItems) {
						if (item.scope.uploadPanelId == el.id)
							listenersPresent = true;
					});
					if (!listenersPresent)
						this.uploader.on('allfinished', callback);

					this.onUpload();
				}
				else
				{ // все аттачи в панели загружены
					callback();
				}
			},

			removeLoadedAttachments: function(callback) {
				if (this.getAttachmentsIds())
				{ // есть загруженные на сервер аттачи -> удалить их
					this.on('queueclear', callback);
					this.onRemoveAllClick();
				}
				else
				{ // нет загруженных на сервер аттачей
					callback();
				}
			},

			listeners: {
				'beforefileremove': function(uploadPanel, store, rec) {
					if (uploadPanel.readOnly) // запретить удаление для readOnly панели
						return false;
					
					if (rec.data.state == 'done')
					{
						Kefir.ajaxRequest(Kefir.form.UPLOAD_URL, { method: 'remove', id: rec.get('id'), index: store.indexOf(rec) });
					}

					return true; // todo: return true only on ajr success
				},
				'beforequeueclear': function(uploadPanel) {
					var ids = uploadPanel.getAttachmentsIds();					
					if (ids) // если ни один файл на сервер не загружен, не нужно вызывать удаление
						Kefir.ajaxRequest(Kefir.form.UPLOAD_URL, { method: 'removeAll', ids: ids });

					return true; // todo: return true only on ajr success
				},

				'render': function() {
					this.fillStore(config.attachments);
				},

				'nameClicked': function(index) { // download loaded file on it's name clicked
					var id = this.store.getAt(index).get('id');
					location.href = Kefir.contextPath + '/download' + '?id=' + id;
				}
			}
		});

		Kefir.form.MultiUploadPanel.superclass.constructor.apply(this, arguments); // arguments because 2nd argument must be an array
	}
});

Kefir.form.getMultiUploadPanel = function(config, id, entityName, entityFieldName, entityId, width, height) {
	config.baseParams = config.baseParams || {};
	Ext.apply(config.baseParams, { entityName: entityName, entityFieldName: entityFieldName, entityId: entityId });
	
	Ext.apply(config, {
		id: id,
		width: width,
		height: height || 200
	});

	var uploadPanel = new Kefir.form.MultiUploadPanel(config);

	uploadPanel.uploader.uploadPanelId = id;
	uploadPanel.uploader.on('filefinished', function(fileUploader, options, o) {
		Ext.getCmp(fileUploader.uploadPanelId).store.getAt(Kefir.getValue(o, 'index')).set('id', Kefir.getId(o)); // save attachment's id to store record
	});

	if (config.readOnly)
		uploadPanel.setReadOnly();

	return uploadPanel;
}