package me.legault.hdb3.encoding;

public final class HDB3Encoder {

	// This is a utility class
	private HDB3Encoder() {
	}

	public static String encode(String data) {
		int lastConsecutiveZeros = 0;
		boolean evenParity = true; // Since last violation
		boolean firstViolation = false;
		boolean lastPulsePositive = false;
		int n = data.length();
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {
			if (data.charAt(i) == '1') {
				appendConsecutiveZeros(sb, lastConsecutiveZeros);
				sb.append(lastPulsePositive ? '-' : '+');
				lastPulsePositive = !lastPulsePositive;
				lastConsecutiveZeros = 0;
				evenParity = !evenParity;
			} else {
				lastConsecutiveZeros++;
			}

			// We introduce a violation
			if (lastConsecutiveZeros == 4) {
				if (evenParity && !firstViolation) {
					sb.append(lastPulsePositive ? "000+" : "000-");
					evenParity = !evenParity;
				} else {
					sb.append(lastPulsePositive ? "-00-" : "+00+");
					lastPulsePositive = !lastPulsePositive;
				}
				firstViolation = false;
				lastConsecutiveZeros = 0;
			}
		}

		appendConsecutiveZeros(sb, lastConsecutiveZeros);

		return sb.toString();
	}

	public static String decode(String data) {
		boolean lastPulsePositive = false;
		int lastConsecutiveZeros = 0;
		boolean isOneWaiting = false;
		int n = data.length();
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {
			char element = data.charAt(i);
			if (element == '+' || element == '-') {
				boolean pulsePositive = element == '+';
				if (lastPulsePositive != pulsePositive) {
					if(isOneWaiting)
						sb.append('1');
					appendConsecutiveZeros(sb, lastConsecutiveZeros);
					isOneWaiting = true;
					lastPulsePositive = pulsePositive;
					lastConsecutiveZeros = 0;
				}else{
					//We have a violation
					if(lastConsecutiveZeros == 3 && isOneWaiting)
						sb.append("1");
					isOneWaiting = false;
					sb.append("0000");
					lastConsecutiveZeros = 0;
				}
			} else {
				lastConsecutiveZeros++;
			}
		}

		if(isOneWaiting)
			sb.append('1');
		appendConsecutiveZeros(sb, lastConsecutiveZeros);

		return sb.toString();
	}

	private static void appendConsecutiveZeros(StringBuilder sb, int numZeros) {
		for (int j = 0; j < numZeros; j++)
			sb.append('0');
	}
}
