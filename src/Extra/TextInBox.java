/*
 * [The "BSD license"]
 * Copyright (c) 2011, abego Software GmbH, Germany (http://www.abego.org)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the abego Software GmbH nor the names of its 
 *    contributors may be used to endorse or promote products derived from this 
 *    software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package Extra;

/**
 * Represents a text to be displayed in a box of a given size.
 * 
 * @author Udo Borkowski (ub@abego.org)
 */
public class TextInBox {
	boolean choosen;
	public String order;
	public  String text;
	public  String overLinetext;
	public String why;
	public String InboxText;
	public final int height;
	public final int width;

	public TextInBox(boolean _choosen,String reason,String text,String _overLinetext,String _InboxText, int width, int height) {
		this.text = text;
		this.width = width;
		this.height = height;
		choosen=_choosen;
		overLinetext=_overLinetext;
		InboxText=_InboxText;
		why=reason;
	}

	public void SetPref(String p1){
		order=p1;
	}
	
	
	public String getOrder() {
		return order;
	}

	

	public String getWhy() {
		return why;
	}

	public String getOverlineText() {
		// TODO Auto-generated method stub
		return overLinetext;
	}

	public String getInBoxText() {
		// TODO Auto-generated method stub
		return InboxText;
	}
}