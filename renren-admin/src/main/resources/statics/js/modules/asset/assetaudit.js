$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'asset/assetaudit/list',
        datatype: "json",
        colModel: [
            //0 待审批、1 已同意、2 被驳回、 3 --
            { label: '单据状态', name: 'recordStatus', index: 'record_status', width: 80, formatter:function (value, options, row) {
                    if(value === 0)
                        return '<span class="label label-warning">待审批</span>';
                    else if(value === 1)
                        return '<span class="label label-success">已同意</span>';
                    else if(value === 2)
                        return '<span class="label label-default">被驳回</span>';
                    else if(value === 3)
                        return '<span> -- </span>';
                } },
            /*{ label: 'id', name: 'id', index: 'id', width: 50, key: true },*/
            { label: '单号', name: 'recordNo', index: 'record_no', width: 80,
                formatter: function (value, options, row) {
                    return '<a style="cursor: pointer" onclick="vm.recordNoDetail(\'' + value + '\')">' + value + '</a>'
                }
            },
			{ label: 'id', hidden: true, name: 'id', index: 'id', width: 50, key: true },
			{ label: '单据类型', name: 'recordType', index: 'record_type', width: 80, formatter: function(value, options, row){
                    if(value === 0)
                        return '<span>领用</span>';
                    else if(value === 1)
                        return '<span>退还</span>';
                    else if(value === 2)
                        return '<span>借用</span>';
                    else if(value === 3)
                        return '<span>归还</span>';
                    else if(value === 4)
                        return '<span>变更</span>';
                    else if(value === 5)
                        return '<span>调拨</span>';
                    else if(value === 6)
                        return '<span>维修</span>';
                    else if(value === 7)
                        return '<span>报废</span>';
                } },
			{ label: '申请人名', name: 'createdUsername', index: 'created_username', width: 80 },
			{ label: '申请日期', name: 'createdTime', index: 'created_time', width: 80 },
            {
                label: '操作',
                name: 'recordNo',
                index: 'record_no',
                align: 'left',
                width: 80,
                key: true,
                formatter: function (value, options, row) {
                    var str = '<a class="btn btn-primary btn-xs 11" onclick="vm.recordNoDetail(\'' + value + '\')" ><i class="fa fa-pencil-square-o"></i> 审批 </a>';
                    return str;
                }
            }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: false,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		assetAudit: {},
	},
	methods: {

        /**点击单号 查看详情 */
        recordNoDetail: function(value){

            var str = value.substr(0, 2);
            if(str === "BG"){
                //资产变更 弹框
                layer.open({
                    type: 2,
                    title: "审批单号详情",
                    maxmin: true,
                    shadeClose: true,
                    shade: 0.5,
                    area: ['98vw', '98vh'],
                    content: 'transformer_audit.html?recordNo=' + value
                });
            }

            if(str === "DB"){
                //资产调拨 弹框
                layer.open({
                    type: 2,
                    title: "审批单号详情",
                    maxmin: true,
                    shadeClose: true,
                    shade: 0.5,
                    area: ['98vw', '98vh'],
                    content: 'transformer_transfer.html?recordNo=' + value
                });
            }








        },

		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.assetAudit = {};
		},
		/*update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.assetAudit.id == null ? "asset/assetaudit/save" : "asset/assetaudit/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.assetAudit),
                    success: function(r){
                        if(r.code === 0){
                             layer.msg("操作成功", {icon: 1});
                             vm.reload();
                             $('#btnSaveOrUpdate').button('reset');
                             $('#btnSaveOrUpdate').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
               if(!lock) {
                    lock = true;
		            $.ajax({
                        type: "POST",
                        url: baseURL + "asset/assetaudit/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function(r){
                            if(r.code == 0){
                                layer.msg("操作成功", {icon: 1});
                                $("#jqGrid").trigger("reloadGrid");
                            }else{
                                layer.alert(r.msg);
                            }
                        }
				    });
			    }
             }, function(){
             });
		},*/
		getInfo: function(id){
			$.get(baseURL + "asset/assetaudit/info/"+id, function(r){
                vm.assetAudit = r.assetAudit;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                page:page
            }).trigger("reloadGrid");
		}
	}
});
