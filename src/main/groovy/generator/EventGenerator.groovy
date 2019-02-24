package generator;

import static generator.Utils.*
import static generator.Config.*

class EventGenerator {
	static generateMessage() {
		def numOfWords = rand.nextInt(30) + 2;
		def stringMessageLine = ""

		for (def n = 0; n < numOfWords; n++) {
			def wordSize = rand.nextInt(10) + 3;
			stringMessageLine += generateName(wordSize, "", wordSize);
			stringMessageLine += " ";
		}

		return stringMessageLine
	}

	static generateMessages(verb, messagesCount) {
		def result = []

		for (int i = 0; i < messagesCount; i++) {
			result += "logger.$verb(\"Time for log - $verb ${generateMessage()}\");"
		}

		return result
	}

	static generateExceptionXTimes() {
		def times = 1000;
		def lines = "";

		lines += "			for (int i = 0; i < ${times}; i++) \n"
		lines += "			{\n"
		lines += "			   	try\n"
		lines += "				  {\n"
		lines += "				  	throw new BullshifierException(context);\n"
		lines += "		  		}\n"
		lines += "			   	catch (Exception e)\n"
		lines += "				  {\n"
		lines += "				  	e.printStackTrace();\n"
		lines += "			   	}\n"
		lines += "      }\n"

		return lines
	}

	static generateRunTimeException() {
		return "				throw new BullshifierException(context);\n"
	}

	static generateThrowableException() {
		return "				throw new IOException(\"Time for IO exception, context is \" + context);\n"
	}

	static generateLogError() {
		return generateMessages("error", Config.logErrorPerMethod).collect({ "\t\t\t$it" })
	}

	static generateLogWarn() {
		return generateMessages("warn", Config.logWarnPerMethod).collect({ "\t\t\t$it" })
	}

	static generateLogInfo() {
		return generateMessages("info", Config.logInfoPerMethod).collect({ "\t\t\t$it" })
	}

	static generateSuicide() {
		return [
				"System.out.println(\"shouldSuicide.. \");",
				"				 System.exit(0);"]
	}

	static generateIoCpuIntensiveLogic() {
		return IoCpuIntensiveLogicGenerator.generate();
	}

	static addEvent() {
		return [
			"	if (Config.get().shouldWriteLogInfo(context))",
			"	{"] +
					EventGenerator.generateLogInfo() + [
			"	}",
			"",
			"	if (Config.get().shouldDoIoCpuIntensiveLogic(context))",
			"	{"] +
					EventGenerator.generateIoCpuIntensiveLogic() + [
			"	}",
			"",
			"	if (Config.get().shouldThrowSomething(methodId,classId))",
			"	{",
			"		if(Config.get().shouldThrow1000())",
			"		{"] +
					EventGenerator.generateExceptionXTimes() + [
			"		}",
			"",
			"		if (Config.get().shouldWriteLogWarn(context))",
			"		{"] +
						EventGenerator.generateLogWarn() + [
			"		}",
			"",
			"		if (Config.get().shouldWriteLogError(context))",
			"		{"] +
						EventGenerator.generateLogError() + [
			"		}",
			"",
			"		if (Config.get().shouldThrowIllegal(context))",
			"		{"] +
						EventGenerator.generateRunTimeException() + [
			"		}",
			"",
			"		if (Config.get().shouldThrowIO(context))",
			"		{"] +
						EventGenerator.generateThrowableException() + [
			"		}",
			"}",
			"",
			"		if (Config.get().shouldSuicide())",
			"		{"] +
						generateSuicide() + [
			"		}",
			"",
			"	if (Config.get().shouldRunAway(context))",
			"	{",
			"		return;",
			"	}"
		]
	}
}
