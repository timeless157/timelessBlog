package com.timeless.controller;

import com.timeless.domain.ResponseResult;
import com.timeless.domain.entity.Menu;
import com.timeless.domain.vo.MenuTreeSelectVo;
import com.timeless.domain.vo.RoleMenuTreeSelectVo;
import com.timeless.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-17 11:25
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult listBackMenu(String menuName, String status) {
        List<Menu> menus = menuService.listBackMenu(menuName,status);
        return ResponseResult.okResult(menus);
    }

    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getMenuDetail(@PathVariable Long id){
        Menu menu = menuService.getById(id);
        return ResponseResult.okResult(menu);
    }

    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        menuService.updateMenu(menu);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable Long menuId){
        menuService.deleteMenu(menuId);
        return ResponseResult.okResult();
    }

    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        List<MenuTreeSelectVo> list = menuService.treeSelect();
        return ResponseResult.okResult(list);
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeSelect(@PathVariable Long id){
        RoleMenuTreeSelectVo roleMenuTreeselectVo = menuService.roleMenuTreeSelect(id);
        return ResponseResult.okResult(roleMenuTreeselectVo);
    }

}
