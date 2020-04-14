$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'asset/asset/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '资产编码', name: 'assetCode', index: 'asset_code', width: 80 }, 			
			{ label: '资产名称', name: 'assetName', index: 'asset_name', width: 80 }, 			
			{ label: '资产分类id', name: 'categoryId', index: 'category_id', width: 80 }, 			
			{ label: '资产分类名称', name: 'categoryName', index: 'category_name', width: 80 }, 			
			{ label: '所属组织id', name: 'orgId', index: 'org_id', width: 80 }, 			
			{ label: '所属组织名称', name: 'orgName', index: 'org_name', width: 80 }, 			
			{ label: '使用组织id', name: 'useOrgId', index: 'use_org_id', width: 80 }, 			
			{ label: '使用组织名称', name: 'useOrgName', index: 'use_org_name', width: 80 }, 			
			{ label: '使用人id, 从使用组织里面去查找', name: 'empId', index: 'emp_id', width: 80 }, 			
			{ label: '使用人名称', name: 'empName', index: 'emp_name', width: 80 }, 			
			{ label: '管理员id', name: 'adminUserid', index: 'admin_userid', width: 80 }, 			
			{ label: '管理员名称', name: 'adminUsername', index: 'admin_username', width: 80 }, 			
			{ label: '区域id', name: 'areaId', index: 'area_id', width: 80 }, 			
			{ label: '区域名称', name: 'areaName', index: 'area_name', width: 80 }, 			
			{ label: '存放地点', name: 'storagePosition', index: 'storage_position', width: 80 }, 			
			{ label: '规格型号', name: 'standard', index: 'standard', width: 80 }, 			
			{ label: '计量单位', name: 'unit', index: 'unit', width: 80 }, 			
			{ label: '价值（元）', name: 'worth', index: 'worth', width: 80 }, 			
			{ label: '使用期限（月）', name: 'timeLimit', index: 'time_limit', width: 80 }, 			
			{ label: '购买日期', name: 'buyTime', index: 'buy_time', width: 80 }, 			
			{ label: '供应商', name: 'supplier', index: 'supplier', width: 80 }, 			
			{ label: '备注', name: 'remarks', index: 'remarks', width: 80 }, 			
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80 }, 			
			{ label: '0闲置、1在用、2借用、3维修中、4报废、5待审批。', name: 'assetStatus', index: 'asset_status', width: 80 }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
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
		asset: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.asset = {};
		},
		update: function (event) {
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
                var url = vm.asset.id == null ? "asset/asset/save" : "asset/asset/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.asset),
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
                        url: baseURL + "asset/asset/delete",
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
		},
		getInfo: function(id){
			$.get(baseURL + "asset/asset/info/"+id, function(r){
                vm.asset = r.asset;
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