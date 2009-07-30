// Copyright 2009 Google Inc. All Rights Reserved.

package com.google.jstestdriver.eclipse.ui.launch;

import com.google.jstestdriver.Response;
import com.google.jstestdriver.ResponseStream;
import com.google.jstestdriver.TestResult;
import com.google.jstestdriver.TestResultGenerator;
import com.google.jstestdriver.eclipse.ui.views.JsTestDriverView;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import java.util.Collection;

/**
 * @author shyamseshadri@google.com (Shyam Seshadri)
 */
public class EclipseRunTestsResponseStream implements ResponseStream {

  private final TestResultGenerator testResultGenerator;

  public EclipseRunTestsResponseStream(TestResultGenerator testResultGenerator) {
    this.testResultGenerator = testResultGenerator;
  }

  public void finish() {

  }

  public void stream(Response response) {
    final Collection<TestResult> testResults = testResultGenerator
        .getTestResults(response);
    Display.getDefault().asyncExec(new Runnable() {

      public void run() {
        IWorkbenchPage page = PlatformUI.getWorkbench()
            .getActiveWorkbenchWindow().getActivePage();
        try {
          JsTestDriverView view = (JsTestDriverView) page
              .showView("com.google.jstestdriver.eclipse.ui.views.JsTestDriverView");
          for (TestResult testResult : testResults) {
            view.addResult(testResult.getTestCaseName() + "."
                + testResult.getTestName() + " : " + testResult.getResult());
          }
        } catch (PartInitException e) {
          e.printStackTrace();
        }
      }
    });
  }

}