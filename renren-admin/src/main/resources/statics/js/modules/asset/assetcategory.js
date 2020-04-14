var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "categoryId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		assetCategory: {
            parentName:null,
            parentId:0,
            orderNum:0
        }
	},
	methods: {
        getCategory: function(){
            //加载部门树
            $.get(baseURL + "asset/assetcategory/select", function(r){
                ztree = $.fn.zTree.init($("#categoryTree"), setting, r.categoryList);
                var node = ztree.getNodeByParam("categoryId", vm.assetCategory.parentId);
                ztree.selectNode(node);

                vm.assetCategory.parentName = node.name;
            })
        },
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.assetCategory = {parentName:null,parentId:0,orderNum:0};
            vm.getCategory();
		},
        update: function () {
            var categoryId = getCategoryId();
            if(categoryId == null){
                return ;
            }

            $.get(baseURL + "asset/assetcategory/info/"+categoryId, function(r){
                vm.showList = false;
                vm.title = "修改";
                vm.assetCategory = r.assetCategory;

                vm.getCategory();
            });
        },
        del: function () {
            var categoryId = getCategoryId();
            if(categoryId == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "asset/assetcategory/delete",
                    data: "categoryId=" + categoryId,
                    success: function(r){
                        if(r.code === 0){
                            alert('操作成功', function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },

        saveOrUpdate: function (event) {
            var url = vm.assetCategory.categoryId == null ? "asset/assetcategory/save" : "asset/assetcategory/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.assetCategory),
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(){
                            vm.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        categoryTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#categoryLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    vm.assetCategory.parentId = node[0].categoryId;
                    vm.assetCategory.parentName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            Category.table.refresh();
        }
	}
});

var Category = {
    id: "categoryTable",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Category.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: '分类ID', field: 'categoryId', visible: false, align: 'center', valign: 'middle', width: '80px'},
        {title: '分类名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '180px'},
        {title: '上级分类', field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '排序号', field: 'sortNum', align: 'center', valign: 'middle', sortable: true, width: '100px'}]
    return columns;
};


function getCategoryId () {
    var selected = $('#categoryTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        alert("请选择一条记录");
        return null;
    } else {
        return selected[0].id;
    }
}

$(function () {
    $.get(baseURL + "asset/assetcategory/info", function(r){
        var colunms = Category.initColumn();
        var table = new TreeTable(Category.id, baseURL + "asset/assetcategory/list", colunms);
        table.setRootCodeValue(r.categoryId);
        table.setExpandColumn(2);
        table.setIdField("categoryId");
        table.setCodeField("categoryId");
        table.setParentCodeField("parentId");
        table.setExpandAll(false);
        table.init();
        Category.table = table;
    });
});
