import os
import subprocess
import sys
import re

str = ""

for line in sys.stdin:
  if "[java]" in line:
    line = line.replace("     [java] ", " ")
    line = line.strip()
    if line[:1] == ".":
      line = line.replace(".", "TEST PASS - ", 1)
    if line[:2] == "F ":
      line = line.replace("F", " 500 FAIL - ", 1)
    if line[:1] == "X":
      line = line.replace("X", " 404 FAIL - ", 1)
    line = line.replace("FrontPage.TestRoot.TestingSuite.", "...", 1)
    re.sub(r'R:\d+    ', '', line)
    re.sub(r'E:\d+    ', '', line)
    re.sub(r'I:\d+    ', '', line)
    print line