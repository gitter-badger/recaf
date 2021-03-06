module Plugin

import lang::recaf::Recaf;
import lang::recaf::desugar::Recaffeinate;
import lang::recaf::TypeCheck;
import ParseTree;

import IO;
import Message;
import util::IDE;

private str LANG_NAME = "Java Recaffeinated";

// relative to HOME dir
private str RECAF_RUNTIME_HOME = "/CWI/recaf/recaf-runtime";

void main() {
  registerLanguage(LANG_NAME, "recaf", start[CompilationUnit](str src, loc org) {
    return parse(#start[CompilationUnit], src, org);
  });
  
  registerContributions(LANG_NAME, {
    builder(set[Message] (Tree tree) {
      if (start[CompilationUnit] cu := tree) {
        loc l = cu@\loc.top;
        l.extension = "java";
        newLoc = |project://recaf-runtime/src/test-generated/generated/<l.file>|;
        newCU = recaffeinate(cu);
        writeFile(newLoc, newCU);
        
        fileLoc = |home://<RECAF_RUNTIME_HOME>/<newLoc.path>|;
        sourcePaths = [
          |home://<RECAF_RUNTIME_HOME>/src/main/java|,
          |home://<RECAF_RUNTIME_HOME>/src/test-generated|
        ];
        return typeCheck(fileLoc, sourcePaths, newCU, cu);
      }
      return {error("Not a <LANG_NAME> program", tree@\loc)};
    })
  });
}