
Ext.ns('Ext.ux.form');

Ext.ux.form.ExtendedUploadField = Ext.extend(Ext.ux.form.FileUploadField,{
    //overrides
    onRender : function(ct, position){
        Ext.ux.form.FileUploadField.superclass.onRender.call(this, ct, position);

        this.wrap = this.el.wrap({cls:'x-form-field-wrap x-form-file-wrap'});
        this.el.addClass('x-form-file-text');
        this.el.dom.removeAttribute('name');
        this.createFileInput();

        var btnCfg = Ext.applyIf(this.buttonCfg || {}, {
            text: this.buttonText
        });
        this.button = new Ext.Button(Ext.apply(btnCfg, {
            renderTo: this.wrap,
            cls: 'x-form-file-btn' // + (btnCfg.iconCls ? ' x-btn-icon' : '')
        }));

        if(this.buttonOnly){
            this.el.hide();
            this.wrap.setWidth(this.button.getEl().getWidth());
        }

        this.bindListeners();
        this.resizeEl = this.positionEl = this.wrap;
    },
    //additional
    /**
        * Detaches the input file without clearing the value so that it can be used for
        * other purposes (e.g. uploading).
        *
        * The returned input file has all listeners applied to it by this class removed.
        * @return {Ext.Element} the detached input file element.
        */
   detachFileInput : function(){
       var result = this.fileInput;

       this.fileInput.removeAllListeners();
       this.fileInput = null;
       this.id = Ext.id(); //avoid dom conflicts
       this.createFileInput();
       this.bindListeners();

       return result;
   },

   /**
        * @return {Ext.Element} the input file element
        */
   getFileInput: function(){
       return this.fileInput;
   }
});
Ext.reg('extendeduploadfield', Ext.ux.form.ExtendedUploadField);