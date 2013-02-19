package ro.bogdan.androidutils.anrdetector;

import java.util.ArrayList;
import java.util.List;

import ro.bogdan.anrdetectorutils.Trace;

public class Rule {
	public enum RuleType {
		TRACE_CONTAINS, TRACE_NOT_CONTAINS
	}

	private RuleType type;
	private String what;

	public Rule(RuleType type, String what) {
		this.type = type;
		this.what = what;
	}

	public String getWhat() {
		return what;
	}

	public RuleType getType() {
		return type;
	}

	public boolean matchesTrace(Trace trace) {
		switch (type) {
			case TRACE_CONTAINS:
				return trace.getFlattenedTrace().contains(what);
			case TRACE_NOT_CONTAINS:
				return !trace.getFlattenedTrace().contains(what);

			default:
				return true;
		}
	}

	/**
	 * Contains a list of methods that we want our traces never to have.(They
	 * indicate false positives) </br>Default: </br>"Handler.handleCallback"
	 * </br>"MessageQueue.next" </br>"MessageQueue.nativePollOnce"
	 * 
	 * @return list of ignore texts (RuleType.TRACE_CONTAINS)
	 */
	public static List<Rule> getDefaultFalsePositivesRuleList() {
		List<Rule> ignoreRuleList = new ArrayList<Rule>();
		ignoreRuleList.add(new Rule(RuleType.TRACE_NOT_CONTAINS, "MessageQueue.nativePollOnce"));
		ignoreRuleList.add(new Rule(RuleType.TRACE_NOT_CONTAINS, "MessageQueue.next"));
		return ignoreRuleList;
	}
}
