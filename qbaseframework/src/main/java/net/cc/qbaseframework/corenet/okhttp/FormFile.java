package net.cc.qbaseframework.corenet.okhttp;

import java.io.File;
import java.io.Serializable;

/**
 * @ClassName FormFile
 * @Description 封装的文件实体
 * @author chen chao
 * @date 2016-4-26
 */

public class FormFile implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static final String PARAM_NAME = "file";
	public static final String FORM_DATA_TYPE = "multipart/form-data";

	/*
	 * 文件
	 */
	private File file;
	/*
	 * 请求参数名称
	 */
	private String parameterName;
	/*
	 * 内容类型
	 */
	private String contentType;

	public FormFile(File file, String parameterName, String contentType)
	{
		super();
		this.file = file;
		this.parameterName = parameterName;
		this.contentType = contentType;
	}

	public File getFile()
	{
		return file;
	}

	public String getParameterName()
	{
		return parameterName;
	}

	public String getContentType()
	{
		return contentType;
	}
}
