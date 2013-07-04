package CompareDB;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 08.02.13
 * Time: 18:43
 * To change this template use File | Settings | File Templates.
 */
public class CompareDataBaseMeta {

    final String delimiter = "/*=================================================*/";

    public CompareDataBaseMeta() {
        //
    }

    public void getCompare(MetaDataDB mySQL, MetaDataDB oracle) throws IOException {
        Date date = new Date();
        File fileExport = new File("C:\\Users\\nkornilova\\Desktop\\Info\\Testing\\HIMKI\\Result_compare_structure_" + date.getTime() + ".txt");

        if (!fileExport.exists()) {
            fileExport.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(fileExport);
        String resTemp = "";
        StringBuilder builder = new StringBuilder();
        try {
            //1. Количество таблиц
            builder.append("Количество таблиц:\n").
                    append(DataBaseType.MYSQL + ":").
                    append(mySQL.lTables.size()).
                    append("\n").
                    append(DataBaseType.ORACLE + ":").
                    append(oracle.lTables.size()).
                    append("\n");

            //2. Рассхождения по списку таблиц
            boolean isTblExists, isColumnExists = false;
            if (mySQL.lTables.size() > oracle.lTables.size()) {
                builder.append("В ").
                        append(DataBaseType.MYSQL).
                        append("больше таблиц, чем в ").
                        append(DataBaseType.ORACLE);

            }
            for (MetaTable metaTableMySQL : mySQL.lTables) {
                isTblExists = false;
                for (MetaTable metaTableOracle : oracle.lTables) {
                    if (metaTableOracle.name.equals(metaTableMySQL.name)) {
                        isTblExists = true;
                        //2. Если таблицу нашли, сверим количество колонок в ней
                        builder.append(delimiter).
                                append("\n").
                                append("Таблица ").
                                append(DataBaseType.MYSQL + ":").
                                append(metaTableMySQL.name).
                                append("\n");

                        builder.append("Количество колонок:\n").
                                append(DataBaseType.MYSQL + ":").
                                append(metaTableMySQL.columnCount).
                                append("\n").
                                append(DataBaseType.ORACLE + ":").
                                append(metaTableOracle.columnCount).
                                append("\n");
                        builder.append("Количество записей:\n").
                                append(DataBaseType.MYSQL + ":").
                                append(metaTableMySQL.recordCount).
                                append("\n").
                                append(DataBaseType.ORACLE + ":").
                                append(metaTableOracle.recordCount).
                                append("\n");

                        if (metaTableOracle.recordCount!=metaTableMySQL.recordCount)
                        {
                            builder.append("ERROR: ").
                                    append("В таблице ").
                                    append(metaTableMySQL.name+" ").
                                    append("разное количество записей").
                                    append("\n");

                            if (metaTableOracle.recordCount>metaTableMySQL.recordCount)
                            {
                                builder.append("ERROR: ").
                                        append("В таблице ").
                                        append(metaTableMySQL.name+" ").
                                        append(DataBaseType.ORACLE + ":").
                                        append("больше записей").
                                        append("\n");
                            }
                        }



                        if (metaTableOracle.columnCount<metaTableMySQL.columnCount)
                        {
                            builder.append("ERROR: ").
                                    append("В таблице ").
                                    append(DataBaseType.MYSQL+" ").
                                    append(metaTableMySQL.name+" ").
                                    append("больше колонок, чем в ").
                                    append(DataBaseType.ORACLE).
                                    append("\n");
                        }
                        boolean occurExist=false;
                        //3. Сверим имена колонок
                        for (MetaColumn metaColumnMySQL : metaTableMySQL.listColumns) {
                             if(metaColumnMySQL.name.toUpperCase().equals("OCCUR"))
                             {
                                 occurExist=true;
                             }
                            isColumnExists=false;
                            for (MetaColumn metaColumnOracle : metaTableOracle.listColumns) {
                                   if(metaColumnOracle.name.equals(metaColumnMySQL.name))
                                   {
                                       isColumnExists=true;

                                       if(metaColumnMySQL.dataTypeLengthEquals(metaColumnOracle.dataType, metaColumnOracle.length, DataBaseType.MYSQL, DataBaseType.ORACLE))
                                       {
                                          //
                                       }
                                       else
                                       { builder.append("ERROR: ").
                                                   append("В таблице ").
                                                   append(metaTableMySQL.name+" ").
                                                   append("тип колонки ").
                                                   append(metaColumnMySQL.name+" ").
                                                   append("\n").
                                                   append(DataBaseType.MYSQL+": ").
                                                   append(metaColumnMySQL.dataType).
                                                   append("\n").
                                                   append(DataBaseType.ORACLE+": ").
                                                   append(metaColumnOracle.dataType).
                                                   append("\n");

                                               builder .append("ERROR: ").
                                                       append("В таблице ").
                                                       append(metaTableMySQL.name+" ").
                                                       append("длина колонки ").
                                                       append(metaColumnMySQL.name+" ( " + metaColumnMySQL.dataType +  ")").
                                                       append("\n").
                                                       append(DataBaseType.MYSQL+": ").
                                                       append(metaColumnMySQL.length).
                                                       append(" ( " + metaColumnMySQL.dataType +  ")").
                                                       append("\n").
                                                       append(DataBaseType.ORACLE+": ").
                                                       append(metaColumnOracle.length).
                                                       append(" ( " + metaColumnOracle.dataType +  ")").
                                                       append("\n");

                                       }
                                       if(metaColumnMySQL.isNull<=metaColumnOracle.isNull)
                                       {
                                          //
                                       }
                                       else
                                           builder.append("ERROR: ").
                                                   append("В таблице ").
                                                   append(metaTableMySQL.name+" ").
                                                   append("обязательность колонки ").
                                                   append(metaColumnMySQL.name+" ").
                                                   append("\n").
                                                   append(DataBaseType.MYSQL+": ").
                                                   append(metaColumnMySQL.isNull==1?"не обязательное поле":"обязательное поле").
                                                   append("\n").
                                                   append(DataBaseType.ORACLE+": ").
                                                   append(metaColumnOracle.isNull==1?"не обязательное поле":"обязательное поле").
                                                   append("\n");


                                       if(//metaColumnMySQL.precision<=metaColumnOracle.precision &&
                                               metaColumnMySQL.scale<=metaColumnOracle.scale)
                                       {
                                           //
                                       }
                                       else
                                           builder .append("ERROR: ").
                                                   append("В таблице ").
                                                   append(metaTableMySQL.name+" ").
                                                   append("точность/разрядность колонки ").
                                                   append(metaColumnMySQL.name).
                                                   append("\n").
                                                   append(DataBaseType.MYSQL+": ").
                                                   append(metaColumnMySQL.precision+", ").append(metaColumnMySQL.scale).
                                                   append("( " + metaColumnMySQL.dataType +  ")").
                                                   append("\n").
                                                   append(DataBaseType.ORACLE+": ").
                                                   append(metaColumnOracle.precision+", ").append(metaColumnOracle.scale).
                                                   append("( " + metaColumnOracle.dataType +  ")").
                                                   append("\n");

                                       break;
                                   }
                            }

                            if (!isColumnExists) {
                                builder.append("ERROR: ").
                                        append("В таблице ").
                                        append(DataBaseType.ORACLE+" ").
                                        append(metaTableOracle.name+" ").
                                        append("нет следующей колонки ").
                                        append(metaColumnMySQL.name).
                                        append("\n");
                            }

                        }
                        if(!occurExist)
                        {

                            builder.append("ERROR: ").
                                    append("В таблице ").
                                    append(DataBaseType.MYSQL+" ").
                                    append(metaTableMySQL.name+" ").
                                    append("нет поля occur").
                                    append("\n");
                        }

                        break;
                    }
                }

                if (!isTblExists) {
                    builder.append("ERROR: ").
                            append(DataBaseType.ORACLE + "нет следующей таблицы:\n").
                            append(metaTableMySQL.name)
                            .append("\n");
                }

            }


            fileWriter.write(builder.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            fileWriter.close();
        }


    }
}
