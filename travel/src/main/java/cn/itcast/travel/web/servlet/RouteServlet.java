package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceIpml;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "/route/*")
public class RouteServlet extends BaseServlet {
    //声明RouteService业务对象
    private RouteService routeService = new RouteServiceIpml();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受参数
        String cidStr = request.getParameter("cid");
        String pageSizeStr = request.getParameter("pageSize");
        String currentPageStr = request.getParameter("currentPage");
        //接受rname线路名称
        String ranme = request.getParameter("rname");
        ranme = new String(ranme.getBytes("iso-8859-1"),"utf-8");
        //2.处理参数
        int cid = 0;//类别id
        if (cidStr != null && cidStr.length()>0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;//当前页码，如果不传递，则默认为第一页
        if (currentPageStr != null && currentPageStr.length()>0){
            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }
        int pageSize = 0;//每页显示条数，如果不传递，默认每页显示5条记录
        if (pageSizeStr != null && pageSizeStr.length()>0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else {
            pageSize = 5;
        }
        //3. 调用service查询PageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid,currentPage,pageSize,ranme);
        //4. 将pageBean对象序列化为json，返回
        writeValue(pb,response);

    }

    /**
     * 根据id查询一个旅游线路的详细信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受参数
        String rid = request.getParameter("rid");
        //3. 调用service查询route对象
        Route route = routeService.findOne(rid);
        //4. 将proute对象序列化为json，返回
        writeValue(route,response);

    }
    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取路线id
        String rid = request.getParameter("rid");
        //2. 获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if (user == null){
            //用户尚未登录
            uid = 0;
        } else {
            //用户已经登录
            uid = user.getUid();
        }
        //3.调用FavoriteService查询是否收藏
        boolean flag = favoriteService.isFavorite(rid,uid);
        //4. 将proute对象序列化为json，返回
        writeValue(flag,response);

    }
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取路线id
        String rid = request.getParameter("rid");
        System.out.println(rid);
        //2. 获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if (user == null){
            //用户尚未登录
            return;
        } else {
            //用户已经登录
            uid = user.getUid();
        }
        //3.调用FavoriteService添加
        favoriteService.add(rid,uid);
    }
}
