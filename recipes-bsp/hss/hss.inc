def hss_get_do_compile_depends(d):
    machine_type = d.getVar('MACHINE_TYPE')

    if machine_type is None:
        return ""

    if 'smp' in machine_type:
        return "u-boot:do_deploy"
    if 'amp' in machine_type:
        return "u-boot:do_deploy mpfs-amp-examples:do_deploy"

    return ""
