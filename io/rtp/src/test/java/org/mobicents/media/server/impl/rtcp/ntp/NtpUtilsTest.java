package org.mobicents.media.server.impl.rtcp.ntp;

import org.apache.commons.net.ntp.TimeStamp;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests NTP functions for RTCP
 * 
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 * 
 */
public class NtpUtilsTest {

	/**
	 * Test correctness of RTCP SR timestamps based on the middle 32-bit of an
	 * NTP timestamp.
	 * 
	 * Using the values of the example provided by RFC3550: 
	 * 
	 * [10 Nov 1995 11:33:25.125 UTC]       [10 Nov 1995 11:33:36.5 UTC]
     * n                 SR(n)              A=b710:8000 (46864.500 s)
     * ---------------------------------------------------------------->
     *                    v                 ^
     * ntp_sec =0xb44db705 v               ^ dlsr=0x0005:4000 (    5.250s)
     * ntp_frac=0x20000000  v             ^  lsr =0xb705:2000 (46853.125s)
     *   (3024992005.125 s)  v           ^
     * r                      v         ^ RR(n)
     * ---------------------------------------------------------------->
     *                        |<-DLSR->|
     *                         (5.250 s)
     *     
     * A     0xb710:8000 (46864.500 s)
     * DLSR -0x0005:4000 (    5.250 s)
     * LSR  -0xb705:2000 (46853.125 s)
     * -------------------------------
     * delay 0x0006:2000 (    6.125 s)
     *    
     * @see <a href="http://tools.ietf.org/html/rfc3550#section-6.4.1">RFC3550</a>
	 */
	@Test
	public void testSrTimestamp() {
		// given
		String ntpSec = "b44db705";
		String ntpFrac = "20000000";
		TimeStamp ntp = new TimeStamp(ntpSec.concat(".").concat(ntpFrac));
		
		// when
		long srTs = NtpUtils.calculateLastSrTimestamp(ntp.getSeconds(), ntp.getFraction());
		long srMs = (srTs * 1000) >> 16; 
		
		// then
		Assert.assertEquals(46853125, srMs);
	}

}
