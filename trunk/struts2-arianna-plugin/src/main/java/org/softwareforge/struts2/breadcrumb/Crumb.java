/*
 *  Copyright 2011 - Giovanni Tosto
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */   
package org.softwareforge.struts2.breadcrumb;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Giovanni Tosto
 */

public class Crumb implements Serializable
{
	String name;
	
	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	String action;

	public String getAction() {
		return action;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getMethod() {
		return method;
	}

	String namespace;
	
	String method;
	
	Map	params;
	
	public String getFullyQualifiedId() {
		return namespace + "/" + action + ":" + method;		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((namespace == null) ? 0 : namespace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Crumb other = (Crumb) obj;
//		if (action == null) {
//			if (other.action != null)
//				return false;
//		} else if (!action.equals(other.action))
//			return false;
//		if (method == null) {
//			if (other.method != null)
//				return false;
//		} else if (!method.equals(other.method))
//			return false;
//		if (name == null) {
//			if (other.name != null)
//				return false;
//		} else if (!name.equals(other.name))
//			return false;
//		if (namespace == null) {
//			if (other.namespace != null)
//				return false;
//		} else if (!namespace.equals(other.namespace))
//			return false;
		return getFullyQualifiedId().equals(other.getFullyQualifiedId());
	}
	
	
}
