package am.ik.categolj2.infra.threaddump;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
public class ThreadDumpInfo {
	private String threadName;
	private long threadId;
	private String threadGroup;
	private String threadState;
	private String stackTrace;
}
