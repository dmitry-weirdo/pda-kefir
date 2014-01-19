/*
extjs-3*
    depends on:
     FileUploadField.js
     ExtendedUploadField.js
     FileUploader.js
     UploadPanel.js
     fileuploadfield.css
     filetree.css
*/
Ext.ns('Mrn.petitions');

Mrn.petitions.MultyUploadPanel = Ext.extend(Ext.ux.UploadPanel, {
    /**
     * @cfg {String} workingIconCls iconClass to use for busy indicator
     */
    inputsIds:false
    ,initComponent:function() {
        this.maxLength=30;
        Ext.apply(this,{
             clickRemoveText:'Нажать для удаления'
             ,addText:'Добавить'
             ,clickStopText:'Нажать для остановки'
             ,emptyText:'Нет файлов'
             ,errorText:'Ошибка'
             ,fileQueuedText:'Файл <b>{0}</b> поставлен в очередь загрузки'
             ,fileDoneText:'Файл <b>{0}</b> загружен'
             ,fileFailedText:'Не удалось загрузить файл <b>{0}</b>'
             ,fileStoppedText:'Загрузка файла <b>{0}</b> остановлена пользвателем'
             ,fileUploadingText:'Загрузка файла <b>{0}</b>'
             ,removeAllText:'Удалить все'
             ,removeText:'Удалить'
             ,stopAllText:'Остановить все'
             ,uploadText:'Загрузить'
             ,progressTask:true
         });
        Mrn.petitions.MultyUploadPanel.superclass.initComponent.apply(this, arguments);
        this.addEvents('nameClicked');
    }
    // overrides
    ,onAddFile:function(fu,fileName) {
        var inp = fu.getFileInput();
        if(true !== this.eventsSuspended && false === this.fireEvent('beforefileadd', this, inp)) {
            return;
        }
        fu.detachFileInput();

        fileName = this.getFileName(inp);
        var recId = inp.id;
        if(this.inputsIds)inp.id=this.inputsIds;

        // create new record and add it to store
        var rec = new this.store.recordType({
             input:inp
            ,fileName:fileName
            ,filePath:this.getFilePath(inp)
            ,shortName: Ext.util.Format.ellipsis(fileName, this.maxLength)
            ,fileCls:this.getFileCls(fileName)
            ,state:'queued'
        }, recId);
        rec.commit();
        this.store.add(rec);

        this.syncShadow();

        this.uploadBtn.enable();
        this.removeAllBtn.enable();

        if(true !== this.eventsSuspended) {
            this.fireEvent('fileadd', this, this.store, rec);
        }
        this.doLayout();

    } // eo onAddFile
    ,fillStore:function(records) {
        this.store.removeAll(false);
        Ext.each(records,function(inp,i,a){
            var rec = new this.store.recordType(inp, inp.id);
            rec.commit();
            this.store.add(rec);
        },this);
//        <%--this.store.add(records);--%>
        this.syncShadow();
        this.removeAllBtn.enable();
        this.doLayout();
    } // eo onAddFile
    ,onRemoveFile:function(record) {
        if(true !== this.eventsSuspended && false === this.fireEvent('beforefileremove', this, this.store, record)){
            return;
        }

        // remove DOM elements
        var inp = record.get('input');
        if(inp){
            var wrap = inp.up('em');
            inp.remove();
            if(wrap) {
                wrap.remove();
            }
        }
        // remove record from store
        this.store.remove(record);

        var count = this.store.getCount();
        this.uploadBtn.setDisabled(!count);
        this.removeAllBtn.setDisabled(!count);

        if(true !== this.eventsSuspended) {
            this.fireEvent('fileremove', this, this.store);
            this.syncShadow();
        }
    } // eo function onRemoveFile
    ,onViewClick:function(view, index, node, e) {
        var t = e.getTarget('div:any(.ux-up-icon-queued|.ux-up-icon-failed|.ux-up-icon-done|.ux-up-icon-stopped)');
        if(t) {
            this.onRemoveFile(this.store.getAt(index));
        }
        t = e.getTarget('div.ux-up-icon-uploading');
        if(t) {
            this.uploader.stopUpload(this.store.getAt(index));
        }
        t = e.getTarget('div.ux-up-text');
        if(t) {
            if(node.lastChild.className.indexOf('ux-up-icon-done')!=-1) this.fireEvent('nameClicked',index);
        }
    } // eo function onViewClick
});
Ext.reg('multyUpload', Mrn.petitions.MultyUploadPanel);