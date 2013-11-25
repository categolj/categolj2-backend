package am.ik.categolj2.app.system;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import am.ik.categolj2.infra.threaddump.ThreadDump;

@Controller
@RequestMapping("system")
public class SystemController {
	@RequestMapping(value = "info", method = RequestMethod.GET)
	public String systemProperties(Model model) {
		Map<String, String> env = new TreeMap<>();
		Map<Object, Object> properties = new TreeMap<>();
		env.putAll(System.getenv());
		properties.putAll(System.getProperties());
		model.addAttribute("env", env);
		model.addAttribute("properties", properties);
		return "system/info";
	}

	@RequestMapping(value = "threadDump", method = RequestMethod.GET)
	public String threadDump(Model model) {
		model.addAttribute("threadDumps", ThreadDump.getThreadDump());
		return "system/threadDump";
	}

}
