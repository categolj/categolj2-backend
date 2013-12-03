package am.ik.categolj2.infra.threaddump;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ThreadDumpInfo {
	private String threadName;
	private long threadId;
	private String threadGroup;
	private String threadState;
	private String stackTrace;
}
