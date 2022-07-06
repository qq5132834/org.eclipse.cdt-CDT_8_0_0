/*******************************************************************************
 * Copyright (c) 2008, 2010 Institute for Software, HSR Hochschule fuer Technik  
 * Rapperswil, University of applied sciences and others
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 *  
 * Contributors: 
 * Institute for Software - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.core.parser.tests.rewrite.changegenerator.remove;

import junit.framework.Test;

import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTConstructorInitializer;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTNewExpression;
import org.eclipse.cdt.core.parser.tests.rewrite.changegenerator.ChangeGeneratorTest;
import org.eclipse.cdt.internal.core.dom.rewrite.ASTModification;
import org.eclipse.cdt.internal.core.dom.rewrite.ASTModificationStore;





public class NewInitializerExpressionTest extends ChangeGeneratorTest {

	public NewInitializerExpressionTest(){
		super("Remove New Initializer Expression"); //$NON-NLS-1$
	}
	
	@Override
	public void setUp() throws Exception{
		source = "int *value = new int(5);"; //$NON-NLS-1$
		expectedSource = "int *value = new int();"; //$NON-NLS-1$
		super.setUp();
	}

	@Override
	protected ASTVisitor createModificator(
			final ASTModificationStore modStore) {
		return new ASTVisitor() {
			{
				shouldVisitExpressions = true;
			}
			
			@Override
			public int visit(IASTExpression expression) {
				if (expression instanceof ICPPASTNewExpression) {
					ICPPASTNewExpression newExpression = (ICPPASTNewExpression) expression;
					final IASTNode lit = ((ICPPASTConstructorInitializer) newExpression.getInitializer()).getArguments()[0];
					ASTModification modification = new ASTModification(ASTModification.ModificationKind.REPLACE, lit, null, null);
					modStore.storeModification(null, modification);
				}
				return PROCESS_CONTINUE;
			}
		};
	}
	
	public static Test suite() {
		return new NewInitializerExpressionTest();
		
	}
	
}
