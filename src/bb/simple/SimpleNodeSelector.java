package bb.simple;

import bb.crawl.CrawlException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleNodeSelector {
    private SimpleNodeSelector superSelector;
    private SimpleNodeSelector childSelector;
    private String tag;
    private String path;
    private final Set<Integer> selectedIndexSet=new HashSet<>();
    private final Set<Integer> ignoreIndexSet=new HashSet<>();
    private boolean isID=false;
    private final Map<String, Pattern> matchPatternMap=new HashMap<>();
    private final Map<String, Pattern> notMatchPatternMap=new HashMap<>();
    private final Map<String,String> focusDataKeyMap=new HashMap<>();
    private final List<Element> selectElementList=new ArrayList<>();


    public String getSpliceFocusData(String key){
        if(selectElementList.isEmpty()){
            return "Not Find";
        }
        StringBuilder stb=new StringBuilder();
        for(int i=0;i<selectElementList.size();i++){
            if(i>0){
                stb.append(" ");
            }
            stb.append(getFocusData(key,selectElementList.get(i)));
        }
        return stb.toString();
    }

    public String getDefaultFocusData(String key){
        if(selectElementList.isEmpty()){
            return "Not Find";
        }
        Element element=selectElementList.get(0);
        return getFocusData(key,element);
    }
    public void resetSelectElement(){
        selectElementList.clear();
        if(childSelector!=null){
            childSelector.resetSelectElement();
        }
    }
    public void selectElement(Element relateRootDom){
        if(isID){
            Element rootDom=relateRootDom;
            while(rootDom.parent()!=null){
                rootDom=rootDom.parent();
            }
            Element ee=rootDom.getElementById(tag.replace("#",""));
            if(ee!=null){
                selectElementList.add(ee);
            }
        }else{
            Elements eeList=relateRootDom.children();
            for (Element ce : eeList) {
                if (check(ce)) {
                    selectElementList.add(ce);
                }
            }
        }


        if(childSelector!=null){
            for(Element ee:selectElementList){
                childSelector.selectElement(ee);
            }
        }


    }

    public String getFocusData(String key,Element element){
        String rk=focusDataKeyMap.get(key);
        if(rk==null){
            return "";
        }
        if(rk.equals("#")){
            return element.ownText();
        }else if(rk.equals("%")){
            return element.text();
        }else{
            return element.attr(rk);
        }

    }

    private boolean check(Element ce){
        int i=ce.elementSiblingIndex();

        if(!ce.tagName().equals(tag)){
            return false;
        }

        if(!selectedIndexSet.isEmpty()&&!selectedIndexSet.contains(i)){
            return false;
        }
        if(!ignoreIndexSet.isEmpty()&&ignoreIndexSet.contains(i)){
            return false;
        }


        for(Map.Entry<String,Pattern> entry:matchPatternMap.entrySet()){
            String st=ce.attr(entry.getKey());
            Matcher matcher = entry.getValue().matcher(st);
            if(!matcher.find()){
                return false;
            }
        }




        for(Map.Entry<String,Pattern> entry:notMatchPatternMap.entrySet()){
            String st=ce.attr(entry.getKey());
            Matcher matcher = entry.getValue().matcher(st);
            if(matcher.find()){
                return false;
            }
        }

        return true;
    }


    public void parse(String selectText, SimpleNodeSelector superSelector) throws CrawlException {
        if(selectText==null||selectText.isEmpty()){
            throw new CrawlException("the selector expression is empty");
        }
        this.superSelector=superSelector;
        int dotIndex=selectText.indexOf("`");
        if(dotIndex==0){
            throw new CrawlException("the selector expression is error {"+selectText+"}");
        }
        String mytext=null;
        if(dotIndex<0){
            mytext=selectText;
            selectText=null;
        }else{
            mytext=selectText.substring(0,dotIndex);
            try{
                selectText=selectText.substring(dotIndex+1);
            }catch (Exception e){
                throw new CrawlException("the sub selector expression is error {"+selectText+"}");
            }
        }
        doParse(mytext);
        if(selectText!=null){
            childSelector=new SimpleNodeSelector();
            childSelector.parse(selectText,this);
        }
    }
    private boolean isInvalid(char c){
        return c=='\n'||c=='\r'||c=='\t';
    }

    public String getPath() {
        return path;
    }

    private List<String> parseSelectText(String selectText){
        List<String> li=new ArrayList<>();
        int startIndex=selectText.indexOf("(#");
        int endIndex=selectText.indexOf("#)");
        while(startIndex>=0){
            String regex=selectText.substring(startIndex+2,endIndex);
            li.add(regex);
            selectText=selectText.replace(regex,"").replace("(##)","");
            startIndex=selectText.indexOf("(#");
            endIndex=selectText.indexOf("#)");

        }
        li.add(0,selectText);
        return li;
    }


    private void parseAttributeRegex(String regex){
        if(regex==null||regex.isEmpty()){
            return;
        }
        StringBuilder stb=new StringBuilder();
        char[] cca=regex.toCharArray();
        String attrName=null;
        Map<String,Pattern> mm=matchPatternMap;
        for(int i=0;i<cca.length;i++){
            char c=cca[i];

            if(attrName!=null){
                stb.append(c);
                continue;
            }


            if(c=='='||c=='!'){
                if(c=='!'){
                    mm=notMatchPatternMap;
                }
                attrName=stb.toString();
                stb=new StringBuilder();
            }else{
                stb.append(c);
            }
        }

        regex=stb.toString();
        if(attrName == null || attrName.isEmpty() || regex.isEmpty()){
            return;
        }

        Pattern pattern=Pattern.compile(regex);
        mm.put(attrName,pattern);
    }

    private void doParse(String selectText){
        List<String> selectTextList=parseSelectText(selectText);
        if(selectTextList.isEmpty()){
            return;
        }
        selectText=selectTextList.remove(0);
        for(String ss:selectTextList){
            parseAttributeRegex(ss);
        }


        char[] charArray=selectText.toCharArray();
        StringBuilder tagBuild=new StringBuilder();
        StringBuilder indexBuild=new StringBuilder();
        StringBuilder extractBuild=new StringBuilder();
        StringBuilder curBuild=tagBuild;
        for(int i=0;i<charArray.length;i++){
            char cc=charArray[i];
            if(isInvalid(cc)){
                continue;
            }
            if(cc=='['){
                curBuild=indexBuild;
            }else if(cc==']'){
                curBuild=null;
            }else if(cc=='{'){
                curBuild=extractBuild;
            }else if(cc=='}'){
                curBuild=null;
            }else if(curBuild!=null){
                curBuild.append(cc);
            }
        }


        tag=tagBuild.toString();

        if(superSelector==null){
            path=tag;
        }else{
            path=superSelector.getPath()+"/"+tag;
        }
        if(tag.startsWith("#")){
            isID=true;
        }


        String tm=indexBuild.toString();
        if(!tm.isEmpty()){
            String[] tmpArray=tm.split(",");
            for(String ta:tmpArray){
                if(ta.contains("-")){
                    String[] t=ta.split("-");
                    Set<Integer> si=selectedIndexSet;
                    if(t[0].contains("i")){
                        si=ignoreIndexSet;
                    }
                    t[0]=t[0].replace("i","");
                    int st=Integer.parseInt(t[0]);
                    int et=Integer.parseInt(t[1]);
                    for(int m=st;m<=et;m++){
                        si.add(m);
                    }
                }else{
                    Set<Integer> si=selectedIndexSet;
                    if(ta.contains("i")){
                        si=ignoreIndexSet;
                    }
                    si.add(Integer.parseInt(ta));
                }
            }
        }


        tm=extractBuild.toString();
        if(!tm.isEmpty()){
            String[] ta=tm.split(",");
            for(String t:ta){
                String[] tc=t.split("=");
                focusDataKeyMap.put(tc[0],tc[1]);
            }
        }
    }
    public SimpleNodeSelector getEndNode(){
        if(childSelector!=null){
            return childSelector.getEndNode();
        }
        return this;
    }

    public List<Element> getSelectElementList() {
        return selectElementList;
    }
}
