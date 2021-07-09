package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.DogDAO;
import dao.PlannerDAO;
import dto.CalendarDTO;
import dto.PersonDTO;
import dto.PlannerDTO;

@WebServlet("*.calendar")
public class calendarController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

		String requestURI = request.getRequestURI(); // 프로젝트명 + 파일명,주소명
		String ctxPath = request.getContextPath(); // 프로젝트명
		String url = requestURI.substring(ctxPath.length()); // (프로젝트명 + 파일명,주소명) - 프로젝트명 = 파일명, 주소명 
		System.out.println(url);
		
		try {
			PlannerDAO Pdao = PlannerDAO.getInstance();
			DogDAO ddao = DogDAO.getInstance();
			
			if(url.contentEquals("/event.calendar")) { // 달력이벤트용 ajax사용
				response.setContentType("text/html;charset=utf-8");
				PersonDTO dto = (PersonDTO)request.getSession().getAttribute("login");

				String local = dto.getLocal();

				Gson g = new Gson();

				List<PlannerDTO> list = Pdao.calendarlist(local);

				List<CalendarDTO> calendar = new ArrayList<>();

				for(PlannerDTO p : list){
					CalendarDTO Cdto = new CalendarDTO();
					String color;
					if(p.getPet_feature().contentEquals("흥분하고 짖어요")) {
						color = "tomato";
					}else if(p.getPerson_id().contentEquals(dto.getId())) {
						color = "#52734D";	
					}else {
						color = "royalblue";
					}
					System.out.println("1");
					Cdto.setId(p.getSeq());
					System.out.println("2");
					Cdto.setTitle(p.getPlace_name());
					System.out.println("3");
					Cdto.setStart(p.getStart());
					System.out.println("4");
					Cdto.setEnd(p.getEnd());
					System.out.println("5");
					Cdto.setColor(color);
					System.out.println(p.getPerson_id());
					
					Cdto.setExtendedProps(ddao.information(p.getPerson_id()));
					System.out.println("7");
					calendar.add(Cdto);
				}
				String result = g.toJson(calendar);
				response.getWriter().append(result);
			}
		}catch (Exception e) {
			
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
