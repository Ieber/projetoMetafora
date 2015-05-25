package br.gov.rn.saogoncalo.academico

import java.text.SimpleDateFormat;



class AulaController {

	def index() { }

	def salvar(){
		println params
		def aula = new Aula(params)

		if (aula.save(flush:true)){
			listarMensagem("Aula salva com sucesso!","ok",Long.parseLong(params.turmaDisciplina))
		}
	}

	def listarAulaPorTurma(long id){

		def td = TurmaDisciplina.get(id)

		def aula = Aula.findAllByTurmaDisciplina(td)

		def tiposAula = TipoDeAula.findAll()
		
		
		
	
		render (view: "/aula/listarAula.gsp", model:[turmaDisciplina:td, aula:aula, tipoAula:tiposAula])
	}

	def listarMensagem(String msg , String tipo, long id){

		
		def td = TurmaDisciplina.get(id)

		def aula = Aula.findAllByTurmaDisciplina(td)

		def tiposAula = TipoDeAula.findAll()

		if (tipo == "ok")
			render (view: "/aula/listarAula.gsp", model:[turmaDisciplina:td, aula:aula, tipoAula:tiposAula, ok:msg])
		else
			render (view: "/aula/listarAula.gsp", model:[turmaDisciplina:td, aula:aula, tipoAula:tiposAula, erro:msg])
	}

	def cadastrarFaltas(long id) {
		def aula = Aula.get(id)
		def alunos = Matricula.findAllByTurma(aula.turmaDisciplina.turma)
		render (view: "/aula/cadastrarFaltas.gsp", model:[nomeDosAlunos:alunos, aula:aula])
	}
	def carregarFaltas(long id){
		def aula = Aula.get(id)
		def alunos = Matricula.findAllByTurma(aula.turmaDisciplina.turma)
		def frequencia = Frequencia.findAllByAula(aula)
		println "frequencia ---- " + frequencia
		render (view: "/aula/cadastrarFaltas.gsp", model:[nomeDosAlunos:alunos, aula:aula,frequencia:frequencia])
	}

	def salvarFrequencia() {
		println params
		def aula = Aula.get(Long.parseLong(params.aulaId))

		for (int i = 1; i < params.size() - 3; i++) {

			def idMatricula = Long.parseLong(params.keySet()[i].toString().replaceAll("-faltas", ""))

			def matricula = Matricula.get(idMatricula)

			def frequencia = Frequencia.findByAulaAndMatricula(aula, matricula)

			Frequencia frequenciaFinal = null
				
			if(params.get(params.keySet()[i].toString()) != "0") {
				if(frequencia == null) {
					def newFrequencia = new Frequencia()

					newFrequencia.aula = aula
					newFrequencia.matricula = matricula
					newFrequencia.quantFaltas = Integer.parseInt(params.get(idMatricula.toString() + "-faltas"))

					frequenciaFinal = newFrequencia
				}
				else {
					frequencia.quantFaltas = Integer.parseInt(params.get(idMatricula.toString() + "-faltas"))
					
					frequenciaFinal = frequencia
				}

				if(frequenciaFinal.save(flush:true)) {
					listarMensagem ("Frequencia atualizada com sucesso!" , "ok", aula.turmaDisciplina.id)
				}
			}
			else {
				if(frequencia != null) {
					frequencia.delete()
					listarMensagem ("Frequencia atualizada com sucesso!" , "ok", aula.turmaDisciplina.id)
				}
			}
			
			
		}
	}
}
