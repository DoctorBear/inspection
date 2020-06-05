import Vue from 'vue'
import ElementUI from 'element-ui'

Vue.use(ElementUI)

Vue.component('doc-table',{
    props: ['docTable'],
    data: function(){
        return {
            docs:[],
        }
    },
    template:`
        <el-table
            :data="docs"
            ：header-cell-style="rowClass"
            @row-click="">
            <el-table-column>
        </el-table>
    `,
    methods: {
        tofile: function(){
        }
    }
});